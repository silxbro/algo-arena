package algo_arena.domain.room.exception;

import algo_arena.common.exception.BaseException;
import algo_arena.common.exception.enums.ErrorType;

public class RoomException extends BaseException {

    public RoomException(ErrorType errorType) {
        super(errorType);
    }

    public RoomException(ErrorType errorType, String errorMessage) {
        super(errorType, errorMessage);
    }

    public RoomException(ErrorType errorType, Throwable cause) {
        super(errorType, cause);
    }

    public RoomException(ErrorType errorType, String errorMessage, Throwable cause) {
        super(errorType, errorMessage, cause);
    }
}