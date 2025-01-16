package algo_arena.chat.factory;

import static algo_arena.chat.enums.MessageType.*;

import algo_arena.chat.enums.MessageType;
import algo_arena.chat.template.MessageTemplate;
import algo_arena.room.entity.Room;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SystemMessageFactory {

    private final Map<MessageType, MessageTemplate> messageTemplateMap = new HashMap<>();

    @PostConstruct
    private void init() {
        messageTemplateMap.put(ENTER, (room, memberName) ->
            "[알림] " + memberName + " 님이 입장했습니다.");
        messageTemplateMap.put(EXIT, (room, memberName) ->
            "[알림] " + memberName + " 님이 퇴장했습니다.");
        messageTemplateMap.put(CREATE, (room, memberName) ->
            "[알림] " + memberName + " 님이 <" + room.getName() + "> 방을 생성했습니다.");
        messageTemplateMap.put(CHANGE_HOST, (room, memberName) ->
            "[알림] 방장이 " + room.getHost().getName() + " 님으로 변경되었습니다.");
    }

    public String create(MessageType type, Room room, String memberName) {
        return messageTemplateMap.get(type).getMessage(room, memberName);
    }
}