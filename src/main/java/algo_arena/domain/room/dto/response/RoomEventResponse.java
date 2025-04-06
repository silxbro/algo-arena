package algo_arena.domain.room.dto.response;

import algo_arena.domain.room.entity.Room;
import algo_arena.domain.room.enums.RoomEvent;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomEventResponse {

    private String roomId;
    private List<RoomEvent> events;
    private RoomDetailResponse roomResult;

    public static RoomEventResponse from(String roomId, List<RoomEvent> roomEvents) {
        return RoomEventResponse.builder()
            .roomId(roomId)
            .events(roomEvents)
            .build();
    }

    public static RoomEventResponse from(String roomId, List<RoomEvent> roomEvents, Room room) {
        return RoomEventResponse.builder()
            .roomId(roomId)
            .events(roomEvents)
            .roomResult(RoomDetailResponse.from(room))
            .build();
    }
}