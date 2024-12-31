package algo_arena.member.dto.response;

import algo_arena.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDetailResponse {

    private String email;
    private String nickname;
    private String imgUrl;
    private long testCount;
    private long problemCount;

    public static MemberDetailResponse from(Member member) {
        return MemberDetailResponse.builder()
            .email(member.getEmail())
            .nickname(member.getNickname())
            .imgUrl(member.getImgUrl())
            .testCount(member.getTestCount())
            .problemCount(member.getProblemCount())
            .build();
    }
}