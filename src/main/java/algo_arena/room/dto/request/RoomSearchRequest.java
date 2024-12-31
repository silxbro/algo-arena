package algo_arena.room.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomSearchRequest {

    private String roomName;
    private Integer maxRoomMembers;
    private String languageName;
    private Integer minProblems;
    private Integer maxProblems;
    private Integer minTimeLimit;
    private Integer maxTimeLimit;

}