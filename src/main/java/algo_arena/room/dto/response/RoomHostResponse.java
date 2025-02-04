package algo_arena.room.dto.response;

import algo_arena.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomHostResponse {
    private String name;

    public static RoomHostResponse from(Member member) {
        return RoomHostResponse.builder().name(member.getName()).build();
    }
}
