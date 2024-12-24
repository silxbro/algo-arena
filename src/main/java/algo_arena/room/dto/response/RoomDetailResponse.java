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
    private Integer maxParticipants;
    private RoomProblems problemInfo;
    private RoomHost hostInfo;
    private RoomParticipants participantsInfo;
    private Integer timeLimit;
    private Language language;

    public static RoomDetailResponse from(Room room, List<String> problemTitles, RoomHost host, List<RoomParticipant> participants) {
        RoomProblems roomProblems = RoomProblems.builder().problemNumber(problemTitles.size()).problemNames(problemTitles).build();
        RoomHost roomHost = RoomHost.builder().nickname(host.getNickname()).imgUrl(host.getImgUrl()).build();
        RoomParticipants roomParticipants = RoomParticipants.builder().participantNumber(participants.size()).participants(participants).build();
        return RoomDetailResponse.builder()
            .roomName(room.getName())
            .maxParticipants(room.getMaxParticipants())
            .problemInfo(roomProblems)
            .hostInfo(roomHost)
            .participantsInfo(roomParticipants)
            .timeLimit(room.getTimeLimit())
            .language(room.getLanguage())
            .build();
    }

    @Getter
    @Builder
    public static class RoomProblems {
        private Integer problemNumber;
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
    public static class RoomParticipants {
        private Integer participantNumber;
        private List<RoomParticipant> participants;
    }

    @Getter
    @Builder
    public static class RoomParticipant {
        private String nickname;
        private String imgUrl;
        private Boolean isReady;
    }
}