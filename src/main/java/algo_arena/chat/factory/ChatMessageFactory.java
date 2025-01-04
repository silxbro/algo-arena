package algo_arena.chat.factory;

import static algo_arena.chat.enums.SocketServerMessageType.*;

import algo_arena.chat.dto.ChatMessage;
import algo_arena.chat.enums.ClientMessageType;
import algo_arena.chat.template.MessageTypeTemplate;
import algo_arena.member.entity.Member;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageFactory {

    private final Map<ClientMessageType, MessageTypeTemplate> messageTypeMap = new HashMap<>();

    @PostConstruct
    private void init() {
        messageTypeMap.put(ClientMessageType.CHAT, (ChatMessage message) -> message.setType(CHAT));
        messageTypeMap.put(ClientMessageType.ENTER, (ChatMessage message) -> message.setType(RENEWAL));
        messageTypeMap.put(ClientMessageType.EXIT, (ChatMessage message) -> message.setType(RENEWAL));
        messageTypeMap.put(ClientMessageType.CREATE, (ChatMessage message) -> message.setType(CREATE));
        messageTypeMap.put(ClientMessageType.CLOSE, (ChatMessage message) -> message.setType(CLOSE));
    }

    public ChatMessage createMessage(
        String roomId, Long index, ClientMessageType type, String message, Member sender, List<Member> receivers) {
        ChatMessage chatMessage = ChatMessage.builder()
            .roomId(roomId)
            .index(index)
            .message(message)
            .sender(sender)
            .receivers(receivers)
            .build();
        return messageTypeMap.get(type).setMessageType(chatMessage);
    }

}