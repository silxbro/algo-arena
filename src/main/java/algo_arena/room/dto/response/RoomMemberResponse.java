package algo_arena.room.dto.response;

import algo_arena.room.entity.RoomMember;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomMemberResponse {
    private String name;
    private Boolean isReady;

    public static RoomMemberResponse from(RoomMember roomMember) {
        return RoomMemberResponse.builder()
            .name(roomMember.getMember().getName())
            .isReady(roomMember.getIsReady())
            .build();
    }
}