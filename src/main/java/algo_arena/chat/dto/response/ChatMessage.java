package algo_arena.chat.dto.response;

import algo_arena.chat.enums.MessageType;
import java.sql.Timestamp;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessage {

    private MessageType type;
    private String roomId;
    private Long index;
    private String senderName;
    private String message;

    @Builder.Default
    private Timestamp sendTime = new Timestamp(System.currentTimeMillis());

    public static ChatMessage create(
        MessageType type, String roomId, Long index, String senderName, String message) {
        return ChatMessage.builder()
            .type(type)
            .roomId(roomId)
            .index(index)
            .senderName(senderName)
            .message(message)
            .build();
    }
}