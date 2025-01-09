package algo_arena.chat.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatSendRequest {

    private String message;

}