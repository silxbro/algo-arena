package algo_arena.room.dto.request;

import algo_arena.member.entity.Member;
import algo_arena.room.entity.Room;
import algo_arena.submission.enums.Language;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomCreateRequest {

    private String name;
    private Integer maxRoomMembers;
    private List<Long> problemIds;
    private String languageName;
    private Integer timeLimit; //분 단위

    public Room toEntity(Member host) {
        return Room.builder()
            .name(name)
            .maxRoomMembers(maxRoomMembers)
            .host(host)
            .language(Language.fromName(languageName))
            .timeLimit(timeLimit)
            .build();
    }
}