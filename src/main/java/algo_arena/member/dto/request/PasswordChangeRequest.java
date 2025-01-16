package algo_arena.member.dto.request;

import lombok.Getter;

@Getter
public class PasswordChangeRequest {

    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;

}