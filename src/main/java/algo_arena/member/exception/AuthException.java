package algo_arena.member.exception;

import algo_arena.common.exception.BaseException;
import algo_arena.common.exception.enums.ErrorStatus;

public class AuthException extends BaseException {

    public AuthException(ErrorStatus errorStatus, String message) {
        super(errorStatus, message);
    }

    public AuthException(ErrorStatus errorStatus, String message, Throwable cause) {
        super(errorStatus, message, cause);
    }
}
