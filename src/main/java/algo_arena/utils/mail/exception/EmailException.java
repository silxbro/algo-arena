package algo_arena.utils.mail.exception;

import algo_arena.common.exception.BaseException;
import algo_arena.common.exception.enums.ErrorType;

public class EmailException extends BaseException {

    public EmailException(ErrorType errorType) {
        super(errorType);
    }

    public EmailException(ErrorType errorType, String errorMessage) {
        super(errorType, errorMessage);
    }

    public EmailException(ErrorType errorType, Throwable cause) {
        super(errorType, cause);
    }

    public EmailException(ErrorType errorType, String errorMessage, Throwable cause) {
        super(errorType, errorMessage, cause);
    }
}