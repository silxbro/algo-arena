package algo_arena.room.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomEventResponse {

    private List<RoomEvent> roomEvents;

    public static RoomEventResponse from(List<RoomEvent> roomEvents) {
        return RoomEventResponse.builder()
            .roomEvents(roomEvents)
            .build();
    }
}