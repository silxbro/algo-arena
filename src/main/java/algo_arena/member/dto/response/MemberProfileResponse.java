package algo_arena.member.dto.response;

import algo_arena.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberProfileResponse {

    private String email;
    private String name;
    private String role;

    public static MemberProfileResponse from(Member member) {
        return MemberProfileResponse.builder()
            .email(member.getEmail())
            .name(member.getName())
            .role(member.getRole().getDescription())
            .build();
    }
}