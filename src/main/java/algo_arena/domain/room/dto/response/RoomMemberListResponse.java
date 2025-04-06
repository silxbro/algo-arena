package algo_arena.domain.room.dto.response;

import algo_arena.domain.room.entity.RoomMember;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomMemberListResponse {
    private Integer number;
    private List<RoomMemberResponse> roomMembers;

    public static RoomMemberListResponse from(List<RoomMember> roomMembers) {
        List<RoomMemberResponse> members = roomMembers.stream().map(RoomMemberResponse::from).toList();
        return RoomMemberListResponse.builder().number(members.size()).roomMembers(members).build();
    }
}