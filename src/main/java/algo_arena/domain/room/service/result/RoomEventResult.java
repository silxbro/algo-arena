package algo_arena.domain.room.service.result;

import algo_arena.domain.room.entity.Room;
import algo_arena.domain.room.enums.RoomEvent;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomEventResult {

    RoomEvent roomEvent;
    Room roomResult;

    public static RoomEventResult from(RoomEvent roomEvent) {
        return RoomEventResult.builder()
            .roomEvent(roomEvent)
            .build();
    }

    public static RoomEventResult from(RoomEvent roomEvent, Room roomResult) {
        return RoomEventResult.builder()
            .roomEvent(roomEvent)
            .roomResult(roomResult)
            .build();
    }
}