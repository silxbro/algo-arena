package algo_arena.chat.dto.response;

import algo_arena.chat.enums.SocketServerMessageType;
import algo_arena.member.entity.Member;
import java.sql.Timestamp;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessage {

    private String roomId;
    private Long index;
    private SocketServerMessageType type;
    private String message;
    private Member sender;
    private List<Member> receivers;

    @Builder.Default
    private Timestamp sendTime = new Timestamp(System.currentTimeMillis());

    public ChatMessage setType(SocketServerMessageType type) {
        this.type = type;
        return this;
    }
}