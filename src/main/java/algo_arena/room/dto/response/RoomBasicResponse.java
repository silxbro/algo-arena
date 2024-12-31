package algo_arena.room.dto.response;

import algo_arena.room.entity.Room;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomBasicResponse {
    private String name;
    private Integer maxRoomMembers;
    private Integer problemNumber;
    private Integer memberNumber;
    private String languageName;
    private Integer timeLimit; //분 단위

    public static RoomBasicResponse from(Room room) {
        return RoomBasicResponse.builder()
            .name(room.getName())
            .maxRoomMembers(room.getMaxRoomMembers())
            .problemNumber(room.getRoomProblems().size())
            .memberNumber(room.getRoomMembers().size())
            .languageName(room.getLanguage().getName())
            .timeLimit(room.getTimeLimit())
            .build();
    }
}
