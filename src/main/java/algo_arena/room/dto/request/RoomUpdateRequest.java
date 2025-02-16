package algo_arena.room.dto.request;

import algo_arena.room.entity.Room;
import algo_arena.submission.enums.CodeLanguage;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomUpdateRequest {

    private Integer maxRoomMembers;
    private List<Long> problemIds;
    private String languageName;
    private Integer timeLimit;

    public Room toEntity() {
        return Room.builder()
            .maxRoomMembers(maxRoomMembers)
            .language(CodeLanguage.fromName(languageName))
            .timeLimit(timeLimit)
            .build();
    }
}