package algo_arena.submission.repository;

import algo_arena.submission.entity.PendingSubmission;
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
public class PendingSubmissionRedisRepository {

    private static final String PENDING_SUBMISSION_PREFIX = "room:problem:submissions:";
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private ListOperations<String, Object> opsListOperation;

    @PostConstruct
    private void init() {
        opsListOperation = redisTemplate.opsForList();
    }

    public Long save(String roomId, PendingSubmission pendingSubmission) {
        String key = generateKey(roomId, pendingSubmission.getProblemNumber());
        return opsListOperation.rightPush(key, pendingSubmission);
    }

    public Long set(String roomId, PendingSubmission pendingSubmission) {
        String key = generateKey(roomId, pendingSubmission.getProblemNumber());
        long index = opsListOperation.indexOf(key, pendingSubmission);
        opsListOperation.set(key, index, pendingSubmission);
        return index;
    }

    public List<PendingSubmission> findAllByRoomProblem(String roomId, Long problemNumber) {
        String key = generateKey(roomId, problemNumber);
        List<Object> pendingSubmissions = opsListOperation.range(key, 0, -1);

        return pendingSubmissions.stream()
            .map(object -> objectMapper.convertValue(object, PendingSubmission.class))
            .collect(Collectors.toList());
    }

    public Optional<PendingSubmission> findOne(String roomId, Long problemNumber, String memberName) {
        return findAllByRoomProblem(roomId, problemNumber).stream()
            .filter(pendingSubmission -> memberName.equals(pendingSubmission.getMemberName()))
            .findAny();
    }

    public boolean hasSubmitted(String roomId, Long problemNumber, String memberName) {
        return findOne(roomId, problemNumber, memberName).isPresent();
    }

    public boolean hasSubmittedCorrectly(String roomId, Long problemNumber, String memberName) {
        PendingSubmission pendingSubmission = findOne(roomId, problemNumber, memberName).orElse(null);
        return pendingSubmission != null && pendingSubmission.isCorrectResult();
    }

    public void deleteByRoomProblem(String roomId, Long problemNumber) {
        String key = generateKey(roomId, problemNumber);
        redisTemplate.delete(key);
    }

    private String generateKey(String roomId, Long problemNumber) {
        return PENDING_SUBMISSION_PREFIX + roomId + ":" + problemNumber;
    }
}