package algo_arena.utils.mail.exception;

import algo_arena.common.exception.BaseException;
import algo_arena.common.exception.enums.ErrorStatus;

public class MailException extends BaseException {

    public MailException(ErrorStatus errorStatus, String message) {
        super(errorStatus, message);
    }

    public MailException(ErrorStatus errorStatus, String message, Throwable cause) {
        super(errorStatus, message, cause);
    }
}