package algo_arena.domain.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatSendRequest {

    @NotBlank(message = "채팅 메시지를 입력해 주세요.")
    private String message;

}