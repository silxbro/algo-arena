package algo_arena.room.dto.request;

import algo_arena.room.entity.Room;
import algo_arena.submission.entity.Language;
import java.util.List;
import lombok.Data;

@Data
public class RoomCreateRequest {

    private String name;
    private Integer maxGuests;
    private List<Long> problemIds;
    private Long hostId;
    private String languageName;
    private Integer timeLimit; //분 단위

    public Room toEntity() {
        return Room.builder()
            .name(name)
            .maxParticipants(maxGuests)
            .problemIds(problemIds)
            .hostId(hostId)
            .language(Language.fromName(languageName))
            .timeLimit(timeLimit)
            .build();
    }
}