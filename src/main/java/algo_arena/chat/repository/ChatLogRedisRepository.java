package algo_arena.chat.repository;

import algo_arena.chat.entity.ChatLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatLogRedisRepository {

    private static final String CHAT_LOG_PREFIX = "room:chatLog:";
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private ListOperations<String, Object> opsListOperation;

    @PostConstruct
    private void init() {
        opsListOperation = redisTemplate.opsForList();
    }

    public Long saveByRoomId(String roomId, ChatLog chatLog) {
        String key = generateKey(roomId);
        Long index = opsListOperation.rightPush(key, chatLog) - 1;
        chatLog.initIndex(index);
        return index;
    }

    public List<ChatLog> findAllByRoomId(String roomId) {
        String key = generateKey(roomId);
        List<Object> chatLogs = opsListOperation.range(key, 0, -1);

        return chatLogs.stream()
            .map(object -> objectMapper.convertValue(object, ChatLog.class))
            .collect(Collectors.toList());
    }

    public Optional<ChatLog> findOneByRoomIdAndIndex(String roomId, Long index) {
        String key = generateKey(roomId);
        return Optional.ofNullable(opsListOperation.index(key, index))
            .map(object -> objectMapper.convertValue(object, ChatLog.class));
    }

    public void updateByRoomIdAndIndex(String roomId, Long index, ChatLog chatLog) {
        String key = generateKey(roomId);
        opsListOperation.set(key, index, chatLog);
    }

    public void deleteByRoomId(String roomId) {
        String key = generateKey(roomId);
        redisTemplate.delete(key);
    }

    private String generateKey(String roomId) {
        return CHAT_LOG_PREFIX + roomId;
    }
}