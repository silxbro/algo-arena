package algo_arena.member.dto.request;

import algo_arena.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberRegisterRequest {

    private String email;
    private String password;
    private String confirmPassword;
    private String name;

    public Member toEntity() {
        return Member.builder()
            .email(email)
            .password(password)
            .name(name)
            .build();
    }
}