package algo_arena.member.dto.response;

import algo_arena.member.entity.Member;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberDetailResponse {

    private String email;
    private String nickname;
    private String imgUrl;
    private String role;

    public static MemberDetailResponse from(Member member) {
        return MemberDetailResponse.builder()
            .email(member.getEmail())
            .nickname(member.getNickname())
            .imgUrl(member.getImgUrl())
            .role(member.getRole().getDescription())
            .build();
    }
}