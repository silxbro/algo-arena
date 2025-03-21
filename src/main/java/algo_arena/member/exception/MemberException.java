package algo_arena.member.exception;

import algo_arena.common.exception.BaseException;
import algo_arena.common.exception.enums.ErrorStatus;

public class MemberException extends BaseException {

    public MemberException(ErrorStatus errorStatus, String message) {
        super(errorStatus, message);
    }

    public MemberException(ErrorStatus errorStatus, String message, Throwable cause) {
        super(errorStatus, message, cause);
    }
}