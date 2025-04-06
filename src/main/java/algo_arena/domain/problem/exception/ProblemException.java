package algo_arena.domain.problem.exception;

import algo_arena.common.exception.BaseException;
import algo_arena.common.exception.enums.ErrorType;

public class ProblemException extends BaseException {

    public ProblemException(ErrorType errorType) {
        super(errorType);
    }

    public ProblemException(ErrorType errorType, String errorMessage) {
        super(errorType, errorMessage);
    }

    public ProblemException(ErrorType errorType, Throwable cause) {
        super(errorType, cause);
    }

    public ProblemException(ErrorType errorType, String errorMessage, Throwable cause) {
        super(errorType, errorMessage, cause);
    }
}