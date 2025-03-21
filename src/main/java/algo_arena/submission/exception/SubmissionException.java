package algo_arena.submission.exception;

import algo_arena.common.exception.BaseException;
import algo_arena.common.exception.enums.ErrorStatus;

public class SubmissionException extends BaseException {

    public SubmissionException(ErrorStatus errorStatus, String message) {
        super(errorStatus, message);
    }

    public SubmissionException(ErrorStatus errorStatus, String message, Throwable cause) {
        super(errorStatus, message, cause);
    }
}