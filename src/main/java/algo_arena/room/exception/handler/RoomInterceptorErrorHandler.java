package algo_arena.room.exception.handler;

import static algo_arena.common.exception.enums.ErrorType.*;

import algo_arena.common.exception.BaseException;
import algo_arena.common.exception.enums.ErrorType;
import algo_arena.room.exception.RoomException;
import algo_arena.room.exception.WebSocketException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.KeyException;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import javax.naming.AuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoomInterceptorErrorHandler extends StompSubProtocolErrorHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        Throwable exception = ex;
        if (exception instanceof MessageDeliveryException) {
            exception = exception.getCause();

            if (exception instanceof JwtException) {
                return handleJwtException(exception);
            }
            if (exception instanceof AuthenticationException) {
                return handleAuthenticationException(exception);
            }
            if (exception instanceof RoomException || exception instanceof WebSocketException) {
                return handleBusinessException((BaseException) exception);
            }
        }
        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    private Message<byte[]> handleJwtException(Throwable exception) {
        if (exception instanceof ExpiredJwtException) {
            return sendErrorMessage(TOKEN_EXPIRED);
        }
        if (exception instanceof UnsupportedJwtException) {
            return sendErrorMessage(TOKEN_NOT_SUPPORTED);
        }
        if (exception instanceof MalformedJwtException) {
            return sendErrorMessage(INVALID_TOKEN_FORM);
        }
        if (exception instanceof SignatureException) {
            return sendErrorMessage(INVALID_TOKEN_SIGNATURE);
        }
        if (exception instanceof KeyException) {
            return sendErrorMessage(INVALID_TOKEN_KEY);
        }
        return sendErrorMessage(INVALID_TOKEN);
    }

    private Message<byte[]> handleAuthenticationException(Throwable exception) {
        if (exception instanceof UsernameNotFoundException) {
            return sendErrorMessage(MEMBER_NOT_FOUND);
        }
        return sendErrorMessage(INVALID_TOKEN);
    }

    private Message<byte[]> handleBusinessException(BaseException exception) {
        return sendErrorMessage(exception.getErrorType());
    }

    private Message<byte[]> sendErrorMessage(ErrorType errorType) {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
        accessor.setMessage(errorType.getMessage());
        accessor.setLeaveMutable(true);

        try {
            String json = objectMapper.writeValueAsString(errorType);
            return MessageBuilder.createMessage(json.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
        } catch (JsonProcessingException e) {
            log.error("Failed to convert ErrorResponse to JSON", e);
            return MessageBuilder.createMessage(errorType.getMessage().getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
        }
    }
}