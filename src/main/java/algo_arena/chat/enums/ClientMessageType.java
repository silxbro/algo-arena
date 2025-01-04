package algo_arena.chat.enums;

public enum ClientMessageType {

    CHAT, ENTER, EXIT, CREATE, CLOSE
    ;

    public boolean isEnter() {
        return this == ENTER;
    }

    public boolean isExit() {
        return this == EXIT;
    }

    public boolean isChat() {
        return this == CHAT;
    }
}