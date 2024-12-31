package algo_arena.room.dto.request;

import algo_arena.room.entity.Room;
import algo_arena.submission.entity.Language;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomUpdateRequest {

    private String name;
    private Integer maxRoomMembers;
    private List<Long> problemIds;
    private String languageName;
    private Integer timeLimit;

    public Room toEntity() {
        return Room.builder()
            .name(name)
            .maxRoomMembers(maxRoomMembers)
            .language(Language.fromName(languageName))
            .timeLimit(timeLimit)
            .build();
    }
}