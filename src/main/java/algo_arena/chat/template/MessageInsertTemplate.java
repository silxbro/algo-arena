package algo_arena.chat.template;

import algo_arena.chat.dto.ClientMessage;
import algo_arena.member.entity.Member;

@FunctionalInterface
public interface MessageInsertTemplate {

    void insertMessage(String roomId, ClientMessage clientMessage, Member member);

}