package algo_arena.domain.chat.dto;

import algo_arena.domain.chat.enums.MessageType;
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