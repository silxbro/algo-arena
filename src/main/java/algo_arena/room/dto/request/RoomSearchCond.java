package algo_arena.room.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomSearchCond {

    private String roomName;
    private Integer maxEntrants;
    private String languageName;
    private Integer minProblems;
    private Integer maxProblems;
    private Integer minTimeLimit;
    private Integer maxTimeLimit;

}