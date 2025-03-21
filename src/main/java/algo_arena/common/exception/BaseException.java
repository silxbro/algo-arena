package algo_arena.common.exception;

import algo_arena.common.exception.enums.ErrorStatus;
import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

    private final ErrorStatus errorStatus;

    public BaseException(ErrorStatus errorStatus, String message) {
        super(message);
        this.errorStatus = errorStatus;
    }

    public BaseException(ErrorStatus errorStatus, String message, Throwable cause) {
        super(message, cause);
        this.errorStatus = errorStatus;
    }
}