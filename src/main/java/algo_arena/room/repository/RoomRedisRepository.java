package algo_arena.room.repository;

import algo_arena.room.dto.request.RoomSearchRequest;
import algo_arena.room.entity.Room;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;
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

    public Room save(Room room) {
        String key = generateKey(room.getId());
        opsValueOperation.set(key, room, 24, TimeUnit.HOURS);
        return room;
    }

    public List<Room> findAllBySearch(RoomSearchRequest request) {
        return redisTemplate.keys(ROOM_KEY_PREFIX + "*").stream()
            .map(this::getRoomFromCache)
            .filter(matches(request))
            .collect(Collectors.toList());
    }

    public Optional<Room> findById(String id) {
        String key = generateKey(id);
        return Optional.ofNullable(getRoomFromCache(key));
    }

    public void deleteById(String id) {
        String key = generateKey(id);
        redisTemplate.delete(key);
    }

    public void clear() {
        redisTemplate.delete(redisTemplate.keys(ROOM_KEY_PREFIX + "*"));
    }

    private Room getRoomFromCache(String key) {
        Object roomObject = opsValueOperation.get(key);
        return objectMapper.convertValue(roomObject, Room.class);
    }

    private Predicate<Room> matches(RoomSearchRequest request) {
        return roomNameContains(request.getRoomName())
            .and(maxRoomMembersEq(request.getMaxRoomMembers()))
            .and(languageEq(request.getLanguageName()))
            .and(problemsBetween(request.getMinProblems(), request.getMaxProblems()))
            .and(timeLimitBetween(request.getMinTimeLimit(), request.getMaxTimeLimit()));
    }

    private Predicate<Room> roomNameContains(String roomName) {
        return room -> roomName == null || room.getName().toUpperCase().contains(roomName.toUpperCase());
    }

    private Predicate<Room> maxRoomMembersEq(Integer maxRoomMembers) {
        return room -> maxRoomMembers == null || room.getMaxRoomMembers() <= maxRoomMembers;
    }

    private Predicate<Room> languageEq(String languageName) {
        return room -> languageName == null || room.getLanguage().getName().equals(languageName);
    }

    private Predicate<Room> problemsBetween(Integer minProblems, Integer maxProblems) {
        return room -> (minProblems == null || room.getRoomProblems().size() >= minProblems) &&
            (maxProblems == null || room.getRoomProblems().size() <= maxProblems);
    }

    private Predicate<Room> timeLimitBetween(Integer minTimeLimit, Integer maxTimeLimit) {
        return room -> (minTimeLimit == null || room.getTimeLimit() >= minTimeLimit) &&
            (maxTimeLimit == null || room.getTimeLimit() <= maxTimeLimit);
    }

    private String generateKey(String roomId) {
        return ROOM_KEY_PREFIX + roomId;
    }
}