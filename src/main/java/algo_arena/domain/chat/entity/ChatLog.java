package algo_arena.domain.chat.entity;

import algo_arena.domain.chat.enums.MessageType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@RedisHash("chatLog")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChatLog implements Serializable {

    @Id
    @EqualsAndHashCode.Include
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @Enumerated(EnumType.STRING)
    private MessageType type;

    @Builder.Default
    private Long index = 0L;

    private String senderName;
    private String message;

    @Builder.Default
    private Timestamp sendTime = new Timestamp(System.currentTimeMillis());

    public void updateMessage(String message) {
        this.message = message;
    }

    public void initIndex(Long index) {
        this.index = index;
    }

    public static ChatLog create(MessageType type, String senderName, String message) {
        return ChatLog.builder()
            .type(type)
            .senderName(senderName)
            .message(message)
            .build();
    }
}