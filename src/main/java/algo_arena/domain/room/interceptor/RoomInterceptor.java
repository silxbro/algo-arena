package algo_arena.domain.room.interceptor;

import static algo_arena.common.exception.enums.ErrorType.*;
import static org.springframework.messaging.simp.stomp.StompCommand.*;

import algo_arena.domain.chat.enums.MessageType;
import algo_arena.domain.room.repository.RoomRepository;
import algo_arena.domain.room.exception.RoomException;
import algo_arena.domain.room.exception.WebSocketException;
import algo_arena.utils.jwt.service.JwtTokenUtil;
import algo_arena.utils.jwt.service.JwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class RoomInterceptor implements ChannelInterceptor {

    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final AntPathMatcher antPathMatcher;
    private final RoomRepository roomRepository;

    /**
     * 참여 조건: accessToken, roomId, username
     * 토큰 유효성, 방 유효성, 사용자 유효성 검사
     * 현재 방에 참여하지 않은 상태인 사용자만 연결 가능
     */

    @Override
    // WebSocket 을 통해 들어온 요청이 처리되기 전에 실행됨
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        final StompCommand command = accessor.getCommand();

        String token = accessor.getFirstNativeHeader("token");
        String roomId = accessor.getFirstNativeHeader("roomId");
        String username = accessor.getFirstNativeHeader("username");
        String destination = accessor.getDestination();

        validateUser(token, username);
        validateDestination(destination);
        validateRoom(roomId);

        if (command == CONNECT) {
            if (roomRepository.isMemberInAnyRoom(username)) {
                throw new RoomException(ALREADY_IN_ROOM);
            }
        }

        if (command == SEND) {
            String type = accessor.getFirstNativeHeader("type");
            if (!StringUtils.hasText(type)) {
                throw new WebSocketException(NULL_VALUE);
            }
            if (!MessageType.isValidType(type)) {
                throw new WebSocketException(INVALID_VALUE);
            }
        }
        return message;
    }

    private void validateUser(String token, String accessorUsername) {
        // 토큰 및 사용자이름 정보 누락 검사
        if (!StringUtils.hasText(token) || !StringUtils.hasText(accessorUsername)) {
            throw new WebSocketException(NULL_VALUE);
        }

        // 토큰과 사용자이름 일치 여부 검사
        String username = jwtTokenUtil.extractUsername(token);
        if (!username.equals(accessorUsername)) {
            throw new WebSocketException(INVALID_VALUE);
        }

        // 토큰 유효성 검사
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
        jwtTokenUtil.validateToken(token, userDetails);
    }

    private void validateDestination(String destination) {
        if (!StringUtils.hasText(destination)) {
            throw new WebSocketException(NULL_VALUE);
        }

        if (!antPathMatcher.match("/**/rooms/**/**", destination)) {
            throw new WebSocketException(INVALID_VALUE);
        }
    }

    private void validateRoom(String roomId) {
        if (!StringUtils.hasText(roomId)) {
            throw new WebSocketException(NULL_VALUE);
        }

        if (!roomRepository.existsById(roomId)) {
            throw new RoomException(ROOM_NOT_FOUND);
        }
    }
}