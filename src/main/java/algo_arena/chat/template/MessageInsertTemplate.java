package algo_arena.chat.template;

import algo_arena.chat.dto.request.ClientMessage;
import algo_arena.member.entity.Member;

@FunctionalInterface
public interface MessageInsertTemplate {

    void insertMessage(ClientMessage message, String roomId, Member member);

}