package algo_arena.chat.entity;

import algo_arena.chat.enums.ClientMessageType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@RedisHash("chatLog")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChatLog implements Serializable {

    @Id
    @EqualsAndHashCode.Include
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @Builder.Default
    private Long index = 0L;

    @Enumerated(EnumType.STRING)
    private ClientMessageType type;

    private String message;
    private String senderName;

    @Builder.Default
    private Timestamp sendTime = new Timestamp(System.currentTimeMillis());

    public void updateMessage(String message) {
        this.message = message;
    }

    public void initIndex(Long index) {
        this.index = index;
    }

    public static ChatLog create(ClientMessageType type, String message, String senderName) {
        return ChatLog.builder()
            .type(type)
            .message(message)
            .senderName(senderName)
            .build();
    }
}