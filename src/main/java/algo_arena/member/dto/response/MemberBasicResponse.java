package algo_arena.member.dto.response;

import algo_arena.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberBasicResponse {

    private String email;
    private String nickname;
    private long problemCount;

    public static MemberBasicResponse from(Member member) {
        return MemberBasicResponse.builder()
            .email(member.getEmail())
            .nickname(member.getNickname())
            .problemCount(member.getProblemCount())
            .build();
    }
}