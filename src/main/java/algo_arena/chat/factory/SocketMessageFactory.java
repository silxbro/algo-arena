package algo_arena.chat.factory;

import static algo_arena.chat.enums.ClientMessageType.*;

import algo_arena.chat.dto.request.ClientMessage;
import algo_arena.chat.enums.ClientMessageType;
import algo_arena.chat.template.MessageInsertTemplate;
import algo_arena.member.entity.Member;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SocketMessageFactory {

    private final Map<ClientMessageType, MessageInsertTemplate> messageInsertMap = new HashMap<>();

    @PostConstruct
    private void init() {
        messageInsertMap.put(CHAT, (message, roomId, member) -> {
            message.setSenderNickname(member.getNickname());
        });
        messageInsertMap.put(ENTER, (message, roomId, member) -> {
            message.setMessage("[알림] " + member.getNickname()+ " 님이 입장했습니다.");
        });
        messageInsertMap.put(EXIT, (message, roomId, member) -> {
            message.setMessage("[알림] " + member.getNickname()+ " 님이 퇴장했습니다.");
        });
    }

    public void insertMessage(ClientMessage message, ClientMessageType type, String roomId, Member member) {
        messageInsertMap.get(type).insertMessage(message, roomId, member);
    }
}