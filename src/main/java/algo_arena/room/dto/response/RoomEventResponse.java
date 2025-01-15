package algo_arena.room.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomEventResponse {

    private String roomId;
    private List<RoomEvent> events;

    public static RoomEventResponse from(String roomId, List<RoomEvent> roomEvents) {
        return RoomEventResponse.builder()
            .roomId(roomId)
            .events(roomEvents)
            .build();
    }
}