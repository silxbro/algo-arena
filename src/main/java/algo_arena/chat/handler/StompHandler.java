package algo_arena.chat.handler;

import static org.springframework.messaging.simp.stomp.StompCommand.CONNECT;

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

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;

    @Override
    // WebSocket 을 통해 들어온 요청이 처리되기 전에 실행됨
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        final StompCommand command = accessor.getCommand();

        if (isConnectCommand(command)) {
            String jwtToken = getJwtToken(accessor);
            validateToken(jwtToken);
        }
        return message;
    }

    private void validateToken(String token) {
        String username = jwtTokenUtil.extractUsername(token);
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
        jwtTokenUtil.validateToken(token, userDetails);
    }

    private String getJwtToken(StompHeaderAccessor accessor) {
        return accessor.getFirstNativeHeader("token");
    }

    private boolean isConnectCommand(StompCommand command) {
        return command == CONNECT;
    }
}