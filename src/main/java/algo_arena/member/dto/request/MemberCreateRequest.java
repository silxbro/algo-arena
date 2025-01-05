package algo_arena.member.dto.request;

import algo_arena.member.entity.Member;
import algo_arena.member.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberCreateRequest {

    private String email;
    private String password;
    private String nickname;
    private String imgUrl;

    public Member toEntity() {
        return Member.builder()
            .email(email)
            .password(password)
            .nickname(nickname)
            //TODO: 기본 프로필 이미지 설정
            .imgUrl(imgUrl == null ? "default" : imgUrl)
            .role(Role.USER)
            .build();
    }
}