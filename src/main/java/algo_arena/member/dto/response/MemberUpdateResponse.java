package algo_arena.member.dto.response;

import algo_arena.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberUpdateResponse {

    private Long id;
    private String nickname;
    private String imageUrl;

    public static MemberUpdateResponse from(Member member) {
        return MemberUpdateResponse.builder()
            .id(member.getId())
            .nickname(member.getNickname())
            .imageUrl(member.getImgUrl())
            .build();
    }
}