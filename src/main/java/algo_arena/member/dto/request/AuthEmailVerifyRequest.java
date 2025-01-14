package algo_arena.member.dto.request;

import lombok.Getter;

@Getter
public class AuthEmailVerifyRequest {

    private String email;
    private String authCode;

}