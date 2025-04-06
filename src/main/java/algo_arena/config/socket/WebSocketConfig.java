package algo_arena.config.socket;

import algo_arena.domain.room.exception.handler.RoomInterceptorErrorHandler;
import algo_arena.domain.room.interceptor.RoomInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final RoomInterceptor roomInterceptor;
    private final RoomInterceptorErrorHandler roomInterceptorErrorHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub"); // 메시지를 브로드캐스팅할 경로
        config.setApplicationDestinationPrefixes("/pub"); // 클라이언트 요청 수신 경로
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp") // 클라이언트에서 연결할 엔드포인트
            .setAllowedOriginPatterns("*") // CORS 허용
            .withSockJS(); // SockJS 사용 (브라우저 호환성)
        registry.addEndpoint("/ws-stomp") // 클라이언트에서 연결할 엔드포인트
            .setAllowedOriginPatterns("*"); // CORS 허용
        registry.setErrorHandler(roomInterceptorErrorHandler);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(roomInterceptor);
    }
}