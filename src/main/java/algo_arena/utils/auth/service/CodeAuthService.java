package algo_arena.utils.auth.service;

import static algo_arena.utils.auth.enums.CodeAuthKey.*;
import static algo_arena.utils.auth.enums.CodeAuthStatus.*;

import jakarta.annotation.PostConstruct;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CodeAuthService {

    private final StringRedisTemplate redisTemplate;
    private HashOperations<String, String, String> hashOperations;

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    public boolean isAuthCompleted(String key) {
        String authStatus = hashOperations.get(key, STATUS.name());
        return authStatus.equals(COMPLETED.name());
    }

    public String getAuthCode(String key) {
        return hashOperations.get(key, CODE.name());
    }

    public void setAuthCodeAndExpiration(String key, String code, long duration) {
        hashOperations.put(key, CODE.name(), code);
        hashOperations.put(key, STATUS.name(), INCOMPLETE.name());
        redisTemplate.expire(key, Duration.ofMillis(duration));
    }

    public void markAuthAsCompleted(String key) {
        hashOperations.put(key, STATUS.name(), COMPLETED.name());
    }

    public void clearAuthHistory(String key) {
        hashOperations.delete(key, CODE.name());
        hashOperations.delete(key, STATUS.name());
    }
}