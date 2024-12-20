package algo_arena.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberCreateResponse {

    private final static String DEFAULT_MESSAGE = "회원가입이 완료되었습니다.";
    private String message;
    private String memberEmail;

    public static MemberCreateResponse from(String email) {
        return new MemberCreateResponse(DEFAULT_MESSAGE, email);
    }
    public static MemberCreateResponse from(String message, String email) {
        return new MemberCreateResponse(message, email);
    }
}