package algo_arena.room.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomUpdateResponse {

    private String roomId;
    private String message;

}