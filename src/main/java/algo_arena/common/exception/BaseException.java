package algo_arena.common.exception;

import algo_arena.common.exception.enums.ErrorType;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final ErrorType errorType;

    public BaseException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public BaseException(ErrorType errorType, String errorMessage) {
        super(errorMessage);
        this.errorType = errorType;
    }

    public BaseException(ErrorType errorType, Throwable cause) {
        super(errorType.getMessage(), cause);
        this.errorType = errorType;
    }

    public BaseException(ErrorType errorType, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorType = errorType;
    }
}