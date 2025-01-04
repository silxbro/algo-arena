package algo_arena.chat.template;

import algo_arena.chat.dto.ChatMessage;

@FunctionalInterface
public interface MessageTypeTemplate {

    ChatMessage setMessageType(ChatMessage chatMessage);

}