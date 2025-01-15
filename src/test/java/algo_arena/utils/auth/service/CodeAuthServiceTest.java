package algo_arena.utils.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CodeAuthServiceTest {

    @Autowired
    CodeAuthService codeAuthService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    String key = "testKey";
    String code = "authCode12";
    long duration = 60000L;

    @BeforeEach
    void allClear() {
        stringRedisTemplate.delete(key);
    }

    @Test
    @DisplayName("인증이 완료됨을 확인할 수 있다")
    void isAuthCompleted_true() {
        //given
        stringRedisTemplate.opsForHash().put(key, "STATUS", "COMPLETED");

        //when
        boolean result = codeAuthService.isAuthCompleted(key);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("인증이 완료되지 않음을 확인할 수 있다")
    void isAuthCompleted_false() {
        //given
        stringRedisTemplate.opsForHash().put(key, "STATUS", "INCOMPLETE");

        //when
        boolean result = codeAuthService.isAuthCompleted(key);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("존재하지 않은 키일 때, 인증이 완료되지 않음을 확인할 수 있다")
    void isAuthCompleted_NotExistKey() {
        //given

        //when
        boolean result = codeAuthService.isAuthCompleted(key);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("키를 통해 인증코드를 조회할 수 있다")
    void getAuthCode_ExistKey() {
        //given
        stringRedisTemplate.opsForHash().put(key, "CODE", code);

        //when
        String result = codeAuthService.getAuthCode(key);

        //then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(code);
    }

    @Test
    @DisplayName("존재하지 않은 키로 인증코드를 조회할 경우, null 을 반환한다")
    void getAuthCode_NotExistKey() {
        //given

        //when
        String result = codeAuthService.getAuthCode(key);

        //then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("인증 코드와 만료 시간을 Redis 에 올바르게 설정한다")
    void setAuthCodeAndExpiration() {
        //given

        //when
        codeAuthService.setAuthCodeAndExpiration(key, code, duration);

        String storedCode = (String) stringRedisTemplate.opsForHash().get(key, "CODE");
        String status = (String) stringRedisTemplate.opsForHash().get(key, "STATUS");
        Long expiration = stringRedisTemplate.getExpire(key, TimeUnit.MILLISECONDS);

        //then
        assertThat(storedCode).isEqualTo(code);
        assertThat(status).isEqualTo("INCOMPLETE");
        assertThat(expiration).isGreaterThan(0);
        assertThat(expiration).isLessThanOrEqualTo(duration);
    }

    @Test
    @DisplayName("키를 지정하여 인증 상태를 완료할 수 있다")
    void markAuthAsCompleted_ExistKey() {
        //given
        codeAuthService.setAuthCodeAndExpiration(key, code, duration);

        //when
        codeAuthService.markAuthAsCompleted(key);
        String status = (String) stringRedisTemplate.opsForHash().get(key, "STATUS");

        //then
        assertThat(status).isEqualTo("COMPLETED");
    }

    @Test
    @DisplayName("존재하지 않는 키의 인증 상태를 완료하려고 시도할 경우, 예외가 발생한다")
    void markAuthAsCompleted_NotExistKey() {
        //given

        //when

        //then
        assertThatThrownBy(() -> codeAuthService.markAuthAsCompleted(key))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("인증 이력을 모두 삭제할 수 있다")
    void clearAuthHistory() {
        //given
        codeAuthService.setAuthCodeAndExpiration(key, code, duration);

        //when
        codeAuthService.clearAuthHistory(key);
        Boolean existKey = stringRedisTemplate.hasKey(key);

        //then
        assertThat(existKey).isFalse();
    }
}