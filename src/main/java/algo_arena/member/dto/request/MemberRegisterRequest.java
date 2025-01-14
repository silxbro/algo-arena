package algo_arena.member.dto.request;

import algo_arena.member.entity.Member;
import algo_arena.member.enums.Role;
import lombok.Getter;

@Getter
public class MemberRegisterRequest {

    private String email;
    private String password;
    private String nickname;
    private String imgUrl;

    public Member toEntity() {
        return Member.builder()
            .email(email)
            .password(password)
            .nickname(nickname)
            .imgUrl(imgUrl == null ? "default" : imgUrl)
            .role(Role.USER)
            .build();
    }
}