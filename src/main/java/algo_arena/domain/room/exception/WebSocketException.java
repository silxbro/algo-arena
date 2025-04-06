package algo_arena.domain.room.exception;

import algo_arena.common.exception.BaseException;
import algo_arena.common.exception.enums.ErrorType;

public class WebSocketException extends BaseException {

    public WebSocketException(ErrorType errorType) {
        super(errorType);
    }

    public WebSocketException(ErrorType errorType, String errorMessage) {
        super(errorType, errorMessage);
    }

    public WebSocketException(ErrorType errorType, Throwable cause) {
        super(errorType, cause);
    }

    public WebSocketException(ErrorType errorType, String errorMessage, Throwable cause) {
        super(errorType, errorMessage, cause);
    }
}