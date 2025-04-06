package algo_arena.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberAuthResponse {

    private String token;

    public static MemberAuthResponse from(String token) {
        return MemberAuthResponse.builder()
            .token(token)
            .build();
    }
}