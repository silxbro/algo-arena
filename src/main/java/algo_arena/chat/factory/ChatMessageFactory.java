package algo_arena.chat.factory;

import algo_arena.chat.dto.response.ChatMessage;
import algo_arena.chat.enums.MessageType;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageFactory {

    public ChatMessage createMessage(
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