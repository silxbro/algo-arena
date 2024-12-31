package algo_arena.room.dto.response;

import algo_arena.member.entity.Member;
import algo_arena.room.entity.Room;
import algo_arena.room.entity.RoomMember;
import algo_arena.submission.entity.Language;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class RoomDetailResponse {

    private String roomName;
    private Integer maxRoomMembers;
    private RoomProblems roomProblems;
    private RoomHost roomHost;
    private RoomMembers roomMembers;
    private Integer timeLimit;
    private Language language;

    public static RoomDetailResponse from(Room room, List<String> problemTitles) {
        return RoomDetailResponse.builder()
            .roomName(room.getName())
            .maxRoomMembers(room.getMaxRoomMembers())
            .roomProblems(RoomProblems.from(problemTitles))
            .roomHost(RoomHost.from(room.getHost()))
            .roomMembers(RoomMembers.from(room.getRoomMembers()))
            .timeLimit(room.getTimeLimit())
            .language(room.getLanguage())
            .build();
    }

    @Getter
    @Builder
    public static class RoomProblems {
        private Integer number;
        private List<String> problemTitles;

        public static RoomProblems from(List<String> problemTitles) {
            return RoomProblems.builder().number(problemTitles.size()).problemTitles(problemTitles).build();
        }
    }

    @Getter
    @Builder
    public static class RoomHost {
        private String nickname;
        private String imgUrl;

        public static RoomHost from(Member member) {
            return RoomHost.builder().nickname(member.getNickname()).imgUrl(member.getImgUrl()).build();
        }
    }

    @Getter
    @Builder
    public static class RoomMembers {
        private Integer number;
        private List<RoomMemberResponse> roomMembers;

        public static RoomMembers from(List<RoomMember> roomMembers) {
            List<RoomMemberResponse> members = roomMembers.stream().map(RoomMemberResponse::from).toList();
            return RoomMembers.builder().number(members.size()).roomMembers(members).build();
        }
    }

    @Getter
    @Builder
    public static class RoomMemberResponse {
        private String nickname;
        private String imgUrl;
        private Boolean isReady;

        public static RoomMemberResponse from(RoomMember roomMember) {
            return RoomMemberResponse.builder()
                .nickname(roomMember.getMember().getNickname())
                .imgUrl(roomMember.getMember().getImgUrl())
                .isReady(roomMember.getIsReady())
                .build();
        }
    }
}