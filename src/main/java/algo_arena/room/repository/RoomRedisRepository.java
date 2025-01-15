package algo_arena.room.repository;

import algo_arena.room.dto.redis.RedisRoom;
import algo_arena.room.entity.Room;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoomRedisRepository {

    private static final String ROOM_KEY_PREFIX = "room:";
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private ValueOperations<String, Object> opsValueOperation;

    @PostConstruct
    private void init() {
        opsValueOperation = redisTemplate.opsForValue();
    }

    public void save(Room room) {
        String key = generateKey(room.getId());
        RedisRoom redisRoom = RedisRoom.from(room);
        opsValueOperation.set(key, redisRoom);
    }


    public Optional<Room> findById(String id) {
        String key = generateKey(id);
        return Optional.ofNullable(getRoomFromCache(key))
            .map(RedisRoom::toRoom);
    }

    public void deleteById(String id) {
        String key = generateKey(id);
        redisTemplate.delete(key);
    }

    public void clear() {
        redisTemplate.delete(redisTemplate.keys(ROOM_KEY_PREFIX + "*"));
    }

    private RedisRoom getRoomFromCache(String key) {
        Object roomObject = opsValueOperation.get(key);
        return objectMapper.convertValue(roomObject, RedisRoom.class);
    }

    private String generateKey(String roomId) {
        return ROOM_KEY_PREFIX + roomId;
    }
}