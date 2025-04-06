package algo_arena.domain.member.dto.response;

import algo_arena.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberRegisterResponse {

    private Long id;
    private String email;
    private String name;

    public static MemberRegisterResponse from(Member member) {
        return MemberRegisterResponse.builder()
            .id(member.getId())
            .email(member.getEmail())
            .name(member.getName())
            .build();
    }
}