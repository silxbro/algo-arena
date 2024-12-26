package algo_arena.room.service;

import lombok.Getter;

public class RoomUpdateResult {

    @Getter
    private final State state;
    private String nickname;

    RoomUpdateResult(State state) {
        this.state = state;
    }

    RoomUpdateResult(State state, String nickname) {
        this.state = state;
        this.nickname = nickname;
    }

    public String getMessage() {
        return String.format(state.getMessageFormat(), nickname);
    }

    @Getter
    public enum State {
        ROOM_UPDATED("테스트방 변경이 완료되었습니다."),
        ENTRANT_ENTERED("[%s]님이 입장했습니다."),
        FULL_ROOM("정원이 초과되어 입장할 수 없습니다."),
        ROOM_DELETED("테스트방이 삭제되었습니다."),
        HOST_CHANGED("방장이 [%s]님으로 변경되었습니다."),
        ENTRANT_EXITED("[%s]님이 퇴장했습니다."),
        ;

        private final String messageFormat;

        State(String messageFormat) {
            this.messageFormat = messageFormat;
        }
    }
}