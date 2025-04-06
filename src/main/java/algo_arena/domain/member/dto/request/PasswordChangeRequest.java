package algo_arena.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class PasswordChangeRequest {

    @NotBlank(message = "현재 비밀번호를 입력해 주세요.")
    private String currentPassword;

    @NotBlank(message = "변경할 비밀번호를 입력해 주세요.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,16}$"
        , message = "비밀번호는 8~16자 영문 대/소문자, 숫자를 모두 포함해야 합니다.")
    private String newPassword;

    @NotBlank(message = "확인용 비밀번호를 입력해 주세요.")
    private String confirmNewPassword;

}