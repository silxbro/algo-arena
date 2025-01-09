package algo_arena.chat.dto.response;

import algo_arena.chat.enums.ClientMessageType;
import java.sql.Timestamp;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessage {

    private ClientMessageType type;
    private String roomId;
    private Long index;
    private String senderName;
    private String message;

    @Builder.Default
    private Timestamp sendTime = new Timestamp(System.currentTimeMillis());
}