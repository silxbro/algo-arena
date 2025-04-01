package algo_arena.chat.enums;

import java.util.Arrays;

public enum MessageType {

    CHAT, ENTER, EXIT, CREATE, CHANGE_HOST
    ;

    public static boolean isValidType(String type) {
        return Arrays.stream(MessageType.values())
            .anyMatch(each -> each.name().equals(type));
    }

    public boolean isChatType() {
        return this == CHAT;
    }
}