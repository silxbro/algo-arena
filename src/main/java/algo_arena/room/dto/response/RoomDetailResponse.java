package algo_arena.room.dto.response;

import algo_arena.room.entity.Room;
import algo_arena.submission.enums.CodeLanguage;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomDetailResponse {

    private String roomName;
    private Integer maxRoomMembers;
    private RoomProblemListResponse roomProblems;
    private RoomHostResponse roomHost;
    private RoomMemberListResponse roomMembers;
    private Integer timeLimit;
    private CodeLanguage language;

    public static RoomDetailResponse from(Room room) {
        return RoomDetailResponse.builder()
            .roomName(room.getName())
            .maxRoomMembers(room.getMaxRoomMembers())
            .roomProblems(RoomProblemListResponse.from(room.getRoomProblems()))
            .roomHost(RoomHostResponse.from(room.getHost()))
            .roomMembers(RoomMemberListResponse.from(room.getRoomMembers()))
            .timeLimit(room.getTimeLimit())
            .language(room.getLanguage())
            .build();
    }
}