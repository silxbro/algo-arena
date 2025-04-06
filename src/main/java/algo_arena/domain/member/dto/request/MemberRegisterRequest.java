package algo_arena.domain.member.dto.request;

import algo_arena.domain.member.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class MemberRegisterRequest {

    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,16}$"
        , message = "비밀번호는 8~16자 영문 대/소문자, 숫자를 모두 포함해야 합니다.")
    private String password;

    @NotBlank(message = "확인용 비밀번호를 입력해 주세요.")
    private String confirmPassword;

    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Pattern(regexp = "^[A-Za-z가-힣\\d]{2,10}$"
        , message = "닉네임은 2~10자 영문 대/소문자, 한글, 숫자만 입력할 수 있습니다.")
    private String name;

    public Member toEntity() {
        return Member.builder()
            .email(email)
            .password(password)
            .name(name)
            .build();
    }
}