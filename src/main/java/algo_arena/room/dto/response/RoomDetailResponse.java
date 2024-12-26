package algo_arena.room.dto.response;

import algo_arena.room.entity.Room;
import algo_arena.submission.entity.Language;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class RoomDetailResponse {

    private String roomName;
    private Integer maxEntrants;
    private RoomProblems problemInfo;
    private RoomHost hostInfo;
    private RoomEntrants entrantsInfo;
    private Integer timeLimit;
    private Language language;

    public static RoomDetailResponse from(Room room, List<String> problemTitles, RoomHost host, List<RoomEntrant> entrants) {
        RoomProblems roomProblems = RoomProblems.builder().number(problemTitles.size()).problemNames(problemTitles).build();
        RoomHost roomHost = RoomHost.builder().nickname(host.getNickname()).imgUrl(host.getImgUrl()).build();
        RoomEntrants roomEntrants = RoomEntrants.builder().number(entrants.size()).entrants(entrants).build();
        return RoomDetailResponse.builder()
            .roomName(room.getName())
            .maxEntrants(room.getMaxEntrants())
            .problemInfo(roomProblems)
            .hostInfo(roomHost)
            .entrantsInfo(roomEntrants)
            .timeLimit(room.getTimeLimit())
            .language(room.getLanguage())
            .build();
    }

    @Getter
    @Builder
    public static class RoomProblems {
        private Integer number;
        private List<String> problemNames;
    }

    @Getter
    @Builder
    public static class RoomHost {
        private String nickname;
        private String imgUrl;
    }

    @Getter
    @Builder
    public static class RoomEntrants {
        private Integer number;
        private List<RoomEntrant> entrants;
    }

    @Getter
    @Builder
    public static class RoomEntrant {
        private String nickname;
        private String imgUrl;
        private Boolean isReady;
    }
}