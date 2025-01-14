package algo_arena.member.dto.response;

import algo_arena.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberRegisterResponse {

    private Long id;
    private String email;
    private String nickname;

    public static MemberRegisterResponse from(Member member) {
        return MemberRegisterResponse.builder()
            .id(member.getId())
            .email(member.getEmail())
            .nickname(member.getNickname())
            .build();
    }
}