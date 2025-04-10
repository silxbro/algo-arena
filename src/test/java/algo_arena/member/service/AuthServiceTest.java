package algo_arena.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import algo_arena.domain.member.entity.Member;
import algo_arena.domain.member.repository.MemberRepository;
import algo_arena.domain.member.service.AuthService;
import algo_arena.utils.auth.service.CodeAuthService;
import algo_arena.utils.jwt.service.JwtTokenUtil;
import algo_arena.utils.jwt.service.JwtUserDetailsService;
import algo_arena.utils.mail.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    AuthService authService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CodeAuthService codeAuthService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    EmailService emailService;

    Member member;
    String email = "tester@example.com";
    String password = "password!";
    String name = "tester";

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        codeAuthService.clearAuthHistory(email);

        member = Member.builder().email(email).password(password).name(name).build();
    }

    @Test
    @DisplayName("이메일 인증이 완료된 상태이고 확인용 비밀번호가 일치할 경우, 회원 가입을 할 수 있다")
    void register_EmailVerified() {
        //given
        String confirmPassword = password;
        completeEmailAuth(email, "code", 10000);

        //when
        Member registeredMember = authService.register(member, confirmPassword);

        //then
        assertThat(registeredMember).isNotNull();
        assertThat(registeredMember.getEmail()).isEqualTo(email);
        assertThat(passwordEncoder.matches(password, registeredMember.getPassword())).isTrue();
    }

    @Test
    @DisplayName("이메일 인증이 완료되지 않을 경우, 회원 가입이 되지 않고 예외가 발생한다")
    void register_EmailNotVerified() {
        //given
        String confirmPassword = password;

        //when

        //then
        assertThatThrownBy(() -> authService.register(member, confirmPassword))
            .isInstanceOf(RuntimeException.class);
        assertThat(memberRepository.findByEmail(email).orElse(null)).isNull();
    }

    @Test
    @DisplayName("확인용 비밀번호가 일치하지 않을 경우, 회원 가입이 되지 않고 예외가 발생한다")
    void register_PasswordNotMatch() {
        //given
        String confirmPassword = "password!!";
        completeEmailAuth(email, "code", 10000);

        //when

        //then
        assertThatThrownBy(() -> authService.register(member, confirmPassword))
            .isInstanceOf(RuntimeException.class);
        assertThat(memberRepository.findByEmail(email).orElse(null)).isNull();
    }

    @Test
    @DisplayName("올바른 인증정보, 즉 이메일과 비밀번호로 인증(로그인)할 경우 토큰이 발급된다")
    void login_CredentialsAreValid() {
        //given
        completeEmailAuth(email, "code", 10000);
        authService.register(member, member.getPassword());
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(member.getName());

        //when
        String token = authService.login(email, password);

        //then
        assertThat(token).isNotNull();
        assertThat(jwtTokenUtil.validateToken(token, userDetails)).isTrue();
    }

    @Test
    @DisplayName("가입되지 않은 이메일로 인증(로그인)을 시도할 경우 예외가 발생한다")
    void login_EmailNotRegistered() {
        //given

        //when

        //then
        assertThatThrownBy(() -> authService.login(email, password))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("틀린 비밀번호로 인증(로그인)을 시도할 경우 예외가 발생한다")
    void login_PasswordNotMatch() {
        //given
        completeEmailAuth(email, "code", 10000);
        authService.register(member, member.getPassword());
        String wrongPassword = "wrongPassword!";

        //when

        //then
        assertThatThrownBy(() -> authService.login(email, wrongPassword))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("이메일 인증을 위한 메일을 송신할 수 있다")
    void sendAuthEmail() {
        //given

        //when
        authService.sendAuthEmail(email);

        //then
        assertThat(codeAuthService.getAuthCode(email)).isNotNull();
        assertThat(codeAuthService.isAuthCompleted(email)).isFalse();
    }

    @Test
    @DisplayName("이메일 인증 코드가 일치할 경우, 인증이 완료된다")
    void verifyAuthEmail_CodeMatch() {
        //given
        String authCode = "authCode12";
        codeAuthService.setAuthCode(email, authCode);

        //when
        authService.verifyAuthEmail(email, authCode);

        //then
        assertThat(codeAuthService.isAuthCompleted(email)).isTrue();
    }

    @Test
    @DisplayName("이메일 인증 메일을 보내지 않은 상태에서 이메일 인증을 바로 시도할 경우, 인증에 실패하여 예외가 발생한다")
    void verifyAuthEmail_EmailNotSent() {
        //given

        //when
        String authCode = codeAuthService.getAuthCode(email);

        //then
        assertThat(authCode).isNull();
        assertThatThrownBy(() -> authService.verifyAuthEmail(email, authCode))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("이메일 인증 코드가 일치하지 않을 경우, 인증에 실패하여 예외가 발생한다")
    void verifyAuthEmail_CodeNotMatch() {
        //given
        String authCode = "authCode12";
        codeAuthService.setAuthCode(email, authCode);

        //when
        String wrongAuthCode = "wrongAuthCode";

        //then
        assertThatThrownBy(() -> authService.verifyAuthEmail(email, wrongAuthCode))
            .isInstanceOf(RuntimeException.class);
    }

    private void completeEmailAuth(String email, String code, long duration) {
        codeAuthService.setAuthCode(email, code);
        codeAuthService.markAuthAsCompleted(email);
    }
}