package algo_arena.room.service.result;

import algo_arena.room.entity.Room;
import algo_arena.room.enums.RoomEvent;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomEventResult {

    RoomEvent roomEvent;
    Room roomResult;

    public static RoomEventResult from(RoomEvent roomEvent, Room roomResult) {
        return RoomEventResult.builder()
            .roomEvent(roomEvent)
            .roomResult(roomResult)
            .build();
    }
}