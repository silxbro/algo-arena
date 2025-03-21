package algo_arena.room.exception;

import algo_arena.common.exception.BaseException;
import algo_arena.common.exception.enums.ErrorStatus;

public class RoomException extends BaseException {

    public RoomException(ErrorStatus errorStatus, String message) {
        super(errorStatus, message);
    }

    public RoomException(ErrorStatus errorStatus, String message, Throwable cause) {
        super(errorStatus, message, cause);
    }
}