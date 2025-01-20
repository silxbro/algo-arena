package algo_arena.room.enums;

public enum RoomEvent {

    ENTER, EXIT, FULL, CREATE, DELETE, UPDATE, CHANGE_HOST;

    public boolean isEnter() {
        return this == ENTER;
    }

    public boolean isExit() {
        return this == EXIT;
    }

    public boolean isChangeHost() {
        return this == CHANGE_HOST;
    }

    public boolean isUpdate() {
        return this == UPDATE;
    }
}