package algo_arena.chat.handler;

import static org.springframework.messaging.simp.stomp.StompCommand.CONNECT;

import algo_arena.utils.jwt.service.JwtTokenUtil;
import algo_arena.utils.jwt.service.JwtUserDetailsService;
import java.nio.file.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;

    @SneakyThrows
    @Override
    // WebSocket 을 통해 들어온 요청이 처리되기 전에 실행됨
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        final StompCommand command = accessor.getCommand();

        if (isConnectCommand(command)) {
            if (!isValidUser(accessor)) {
                throw new AccessDeniedException("유효하지 않은 토큰이거나 사용자를 찾을 수 없습니다.");
            }
        }
        return message;
    }

    private boolean isValidUser(StompHeaderAccessor accessor) {
        String jwtToken = getJwtToken(accessor);
        if (jwtToken == null) {
            return false;
        }
        return isExistingUser(jwtToken);
    }

    private boolean isExistingUser(String jwtToken) {
        String username = jwtTokenUtil.extractUsername(jwtToken);
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
        return jwtTokenUtil.validateToken(jwtToken, userDetails);
    }

    private String getJwtToken(StompHeaderAccessor accessor) {
        return accessor.getFirstNativeHeader("token");
    }

    private boolean isConnectCommand(StompCommand command) {
        return command == CONNECT;
    }
}