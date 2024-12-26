package algo_arena.room.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomUpdateResponse {

    private String roomId;
    private String message;

}