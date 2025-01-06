package algo_arena.member.dto.request;

import lombok.Getter;

@Getter
public class VerifyAuthCodeRequest {

    private String email;
    private String authCode;

}