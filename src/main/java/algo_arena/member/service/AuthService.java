package algo_arena.member.service;

import static algo_arena.common.exception.enums.ErrorType.*;

import algo_arena.member.entity.Member;
import algo_arena.member.exception.AuthException;
import algo_arena.member.exception.MemberException;
import algo_arena.member.repository.MemberRepository;
import algo_arena.utils.auth.service.CodeAuthService;
import algo_arena.utils.auth.service.CodeGenerator;
import algo_arena.utils.jwt.service.JwtTokenUtil;
import algo_arena.utils.jwt.service.JwtUserDetailsService;
import algo_arena.utils.mail.exception.EmailException;
import algo_arena.utils.mail.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final EmailService mailService;
    private final CodeAuthService codeAuthService;
    private final CodeGenerator codeGenerator;
    private final PasswordEncoder passwordEncoder;

    private static final String AUTH_EMAIL_TITLE = "[Algo-Arena] 이메일 인증코드 발송 메일입니다.";
    private static final String AUTH_EMAIL_CONTENT_FORMAT = "인증 코드: %s";

    @Value("${spring.mail.auth-code-length}")
    private int authCodeLength;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    @Transactional
    public Member register(Member member, String confirmPassword) {
        if (!codeAuthService.isAuthCompleted(member.getEmail())) {
            throw new AuthException(EMAIL_AUTH_NOT_COMPLETED);
        }
        if (!member.getPassword().equals(confirmPassword)) {
            throw new AuthException(CONFIRM_PASSWORD_MISMATCH);
        }
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new MemberException(EMAIL_ALREADY_REGISTERED);
        }
        if (memberRepository.existsByName(member.getName())) {
            throw new MemberException(NAME_DUPLICATED);
        }
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.changePassword(encodedPassword);
        return memberRepository.save(member);
    }

    public String login(String email, String password) {
        Member member = memberRepository.findByEmail(email).orElse(null);
        if (member == null || !passwordEncoder.matches(password, member.getPassword())) {
            throw new AuthException(LOGIN_FAILED);
        }
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(member.getName());
        return jwtTokenUtil.generateToken(userDetails);
    }

    @Transactional
    public void sendAuthEmail(String email) {
        String authCode = codeGenerator.generateAuthCode(authCodeLength);
        codeAuthService.setAuthCode(email, authCode);

        try {
            mailService.sendEmail(email, AUTH_EMAIL_TITLE, String.format(AUTH_EMAIL_CONTENT_FORMAT, authCode));
        } catch (Exception e) {
            log.error("이메일 발송 실패. 이메일 주소: {}, ERROR 타입: {}", email, e.getClass().getName(), e);
            throw new EmailException(EMAIL_SENDING_ERROR);
        }
    }

    @Transactional
    public void verifyAuthEmail(String email, String enteredCode) {
        if (codeAuthService.isAuthExpired(email, authCodeExpirationMillis)) {
            throw new AuthException(EMAIL_AUTH_TIME_OUT);
        }
        String code = codeAuthService.getAuthCode(email);
        if (code == null) {
            throw new AuthException(EMAIL_AUTH_NOT_FOUND);
        }
        if (!code.equals(enteredCode)) {
            throw new AuthException(WRONG_EMAIL_AUTH_CODE);
        }
        codeAuthService.markAuthAsCompleted(email);
    }
}