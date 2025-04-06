package algo_arena.domain.member.exception;

import algo_arena.common.exception.BaseException;
import algo_arena.common.exception.enums.ErrorType;

public class AuthException extends BaseException {

    public AuthException(ErrorType errorType) {
        super(errorType);
    }

    public AuthException(ErrorType errorType, String errorMessage) {
        super(errorType, errorMessage);
    }

    public AuthException(ErrorType errorType, Throwable cause) {
        super(errorType, cause);
    }

    public AuthException(ErrorType errorType, String errorMessage, Throwable cause) {
        super(errorType, errorMessage, cause);
    }
}
