package algo_arena.domain.member.dto.response;

import algo_arena.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberBasicResponse {

    private String name;

    public static MemberBasicResponse from(Member member) {
        return MemberBasicResponse.builder()
            .name(member.getName())
            .build();
    }
}