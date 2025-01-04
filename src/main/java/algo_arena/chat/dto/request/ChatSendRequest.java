package algo_arena.chat.dto.request;

import algo_arena.chat.enums.ClientMessageType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatSendRequest {

    private final String roomId;
    private final ClientMessageType type;
    private final String message;
    private final Long senderId;

}
