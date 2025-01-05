package algo_arena.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberLoginResponse {

    private String token;

    public static MemberLoginResponse from(String token) {
        return MemberLoginResponse.builder()
            .token(token)
            .build();
    }
}