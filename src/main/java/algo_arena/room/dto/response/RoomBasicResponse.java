package algo_arena.room.dto.response;

import algo_arena.room.entity.Room;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomBasicResponse {
    private String name;
    private Integer maxParticipants;
    private Integer problemNumber;
    private Integer participantNumber;
    private String languageName;
    private Integer timeLimit; //분 단위

    public static RoomBasicResponse from(Room room) {
        return RoomBasicResponse.builder()
            .name(room.getName())
            .maxParticipants(room.getMaxParticipants())
            .problemNumber(room.getProblemIds().size())
            .participantNumber(room.getParticipants().size())
            .languageName(room.getLanguage().getName())
            .timeLimit(room.getTimeLimit())
            .build();
    }
}
