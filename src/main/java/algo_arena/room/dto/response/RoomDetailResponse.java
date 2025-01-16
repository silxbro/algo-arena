package algo_arena.room.dto.response;

import algo_arena.member.entity.Member;
import algo_arena.room.entity.Room;
import algo_arena.room.entity.RoomMember;
import algo_arena.room.entity.RoomProblem;
import algo_arena.submission.entity.Language;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomDetailResponse {

    private String roomName;
    private Integer maxRoomMembers;
    private RoomProblems roomProblems;
    private RoomHost roomHost;
    private RoomMembers roomMembers;
    private Integer timeLimit;
    private Language language;

    public static RoomDetailResponse from(Room room) {
        return RoomDetailResponse.builder()
            .roomName(room.getName())
            .maxRoomMembers(room.getMaxRoomMembers())
            .roomProblems(RoomProblems.from(room.getRoomProblems()))
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
        private List<RoomProblemResponse> roomProblems;

        public static RoomProblems from(List<RoomProblem> roomProblems) {
            List<RoomProblemResponse> problems = roomProblems.stream().map(RoomProblemResponse::from).toList();
            return RoomProblems.builder().number(problems.size()).roomProblems(problems).build();
        }
    }

    @Getter
    @Builder
    public static class RoomProblemResponse {
        private Long number;
        private String title;
        private String link;

        public static RoomProblemResponse from(RoomProblem roomProblem) {
            return RoomProblemResponse.builder()
                .number(roomProblem.getProblem().getNumber())
                .title(roomProblem.getProblem().getTitle())
                .link(roomProblem.getProblem().getLink())
                .build();
        }
    }

    @Getter
    @Builder
    public static class RoomHost {
        private String name;

        public static RoomHost from(Member member) {
            return RoomHost.builder().name(member.getName()).build();
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
        private String name;
        private Boolean isReady;

        public static RoomMemberResponse from(RoomMember roomMember) {
            return RoomMemberResponse.builder()
                .name(roomMember.getMember().getName())
                .isReady(roomMember.getIsReady())
                .build();
        }
    }
}