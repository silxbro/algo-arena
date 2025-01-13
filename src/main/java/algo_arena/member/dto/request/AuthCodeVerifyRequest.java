package algo_arena.member.dto.request;

import lombok.Getter;

@Getter
public class AuthCodeVerifyRequest {

    private String email;
    private String authCode;

}