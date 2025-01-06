package algo_arena.utils.jwt.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

class JwtTokenUtilTest {

    JwtTokenUtil jwtTokenUtil;
    UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtTokenUtil = new JwtTokenUtil();
        userDetails = User.withUsername("testUser")
            .password("password!!!")
            .build();
    }

    @Test
    @DisplayName("토큰으로부터 사용자 이름을 추출할 수 있다")
    void extractUsername() {
        //given
        String token = jwtTokenUtil.generateToken(userDetails);

        //when
        String username = jwtTokenUtil.extractUsername(token);

        //then
        assertThat(username).isEqualTo(userDetails.getUsername());
    }

    @Test
    @DisplayName("토큰으로부터 만료 날짜를 추출할 수 있다")
    void extractExpiration() {
        //given
        long beforeIssue = System.currentTimeMillis();
        String token = jwtTokenUtil.generateToken(userDetails);

        //when
        Date expiration = jwtTokenUtil.extractExpiration(token);

        //then
        assertThat(expiration.getTime()).isGreaterThan(beforeIssue);
    }

    @Test
    @DisplayName("토큰의 유효성 검증 - 유효한 토큰")
    void validateToken_Valid() {
        //given
        String token = jwtTokenUtil.generateToken(userDetails);

        //when
        Boolean isValid = jwtTokenUtil.validateToken(token, userDetails);

        //then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("토큰의 유효성 검증 - 사용자 정보 불일치 토큰")
    void validateToken_Expired() {
        //given
        UserDetails diffUserDetails = User.withUsername("differentUser").password("password!!!").build();
        String diffToken = jwtTokenUtil.generateToken(diffUserDetails);

        //when
        Boolean isValid = jwtTokenUtil.validateToken(diffToken, userDetails);

        //then
        assertThat(isValid).isFalse();

    }
}