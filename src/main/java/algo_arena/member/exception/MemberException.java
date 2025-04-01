package algo_arena.member.exception;

import algo_arena.common.exception.BaseException;
import algo_arena.common.exception.enums.ErrorType;

public class MemberException extends BaseException {

    public MemberException(ErrorType errorType) {
        super(errorType);
    }

    public MemberException(ErrorType errorType, String errorMessage) {
        super(errorType, errorMessage);
    }

    public MemberException(ErrorType errorType, Throwable cause) {
        super(errorType, cause);
    }

    public MemberException(ErrorType errorType, String errorMessage, Throwable cause) {
        super(errorType, errorMessage, cause);
    }
}