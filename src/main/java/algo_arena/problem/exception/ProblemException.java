package algo_arena.problem.exception;

import algo_arena.common.exception.BaseException;
import algo_arena.common.exception.enums.ErrorStatus;

public class ProblemException extends BaseException {

    public ProblemException(ErrorStatus errorStatus, String message) {
        super(errorStatus, message);
    }

    public ProblemException(ErrorStatus errorStatus, String message, Throwable cause) {
        super(errorStatus, message, cause);
    }
}