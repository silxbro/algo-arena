package algo_arena.submission.exception;

import algo_arena.common.exception.BaseException;
import algo_arena.common.exception.enums.ErrorType;

public class SubmissionException extends BaseException {

    public SubmissionException(ErrorType errorType) {
        super(errorType);
    }

    public SubmissionException(ErrorType errorType, String errorMessage) {
        super(errorType, errorMessage);
    }

    public SubmissionException(ErrorType errorType, Throwable cause) {
        super(errorType, cause);
    }

    public SubmissionException(ErrorType errorType, String errorMessage, Throwable cause) {
        super(errorType, errorMessage, cause);
    }
}