package algo_arena.chat.enums;

public enum MessageType {

    CHAT, ENTER, EXIT, CREATE, CHANGE_HOST
    ;

    public boolean isChatType() {
        return this == CHAT;
    }
}