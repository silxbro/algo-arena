package algo_arena.domain.room.dto.response;

import algo_arena.domain.member.entity.Member;
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
