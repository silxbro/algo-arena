package algo_arena.domain.room.enums;

public enum RoomEvent {

    ENTER, EXIT, FULL, CREATE, DELETE, UPDATE, CHANGE_HOST;

    public boolean isChangeHost() {
        return this == CHANGE_HOST;
    }
}