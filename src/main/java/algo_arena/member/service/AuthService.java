package algo_arena.member.service;

import algo_arena.member.entity.Member;
import algo_arena.member.repository.MemberRepository;
import algo_arena.utils.auth.service.CodeAuthService;
import algo_arena.utils.auth.service.CodeGenerator;
import algo_arena.utils.jwt.service.JwtTokenUtil;
import algo_arena.utils.jwt.service.JwtUserDetailsService;
import algo_arena.utils.mail.service.MailService;
import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;
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
    private final MailService mailService;
    private final CodeAuthService codeAuthService;
    private final CodeGenerator codeGenerator;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.mail.auth-code-length}")
    private int authCodeLength;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    @Transactional
    public Member register(Member member, String confirmPassword) {
        if (!codeAuthService.isAuthCompleted(member.getEmail())) {
            throw new RuntimeException("인증되지 않은 이메일 입니다. 이메일 인증을 완료해주세요.");
        }
        if (!member.getPassword().equals(confirmPassword)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다. 다시 입력해 주세요.");
        }
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.changePassword(encodedPassword);
        return memberRepository.save(member);
    }

    public String login(String email, String password) {
        Member member = memberRepository.findByEmail(email).orElse(null);
        if (member == null) {
            throw new RuntimeException("가입되지 않은 이메일입니다.");
        }
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(member.getName());
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new RuntimeException("패스워드가 일치하지 않습니다.");
        }
        return jwtTokenUtil.generateToken(userDetails);
    }

    @Transactional
    public void sendAuthEmail(String email) {
        String authCode = codeGenerator.generateAuthCode(authCodeLength);
        codeAuthService.setAuthCodeAndExpiration(email, authCode, authCodeExpirationMillis);
        String emailTitle = "[Algo-Arena] 이메일 인증코드 발송 메일입니다.";
        String emailContent = "인증 코드: " + authCode;
        try {
            mailService.sendEmail(email, emailTitle, emailContent);
        } catch (MessagingException e) {
            log.error("MessagingException: 이메일 발송 실패 - 이메일 서버와의 통신 오류. 이메일 주소: {}", email, e);
            throw new RuntimeException("이메일 발송 중 서버 통신 오류가 발생했습니다.");
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException: 이메일 발송 실패 - 잘못된 문자 인코딩. 이메일 주소: {}", email, e);
            throw new RuntimeException("이메일 발송 중 인코딩 오류가 발생했습니다.");
        } catch (Exception e) {
            log.error("Exception: 이메일 발송 실패 - 알 수 없는 오류. 이메일 주소: {}", email, e);
            throw new RuntimeException("이메일 발송에 실패했습니다. 다시 시도해 주세요.");
        }
    }

    @Transactional
    public void verifyAuthEmail(String email, String enteredCode) {
        String code = codeAuthService.getAuthCode(email);
        if (code == null) {
            throw new RuntimeException("이메일 인증을 시도해 주세요.");
        }
        if (!code.equals(enteredCode)) {
            throw new RuntimeException("이메일 인증코드가 일치하지 않습니다.");
        }
        codeAuthService.markAuthAsCompleted(email);
    }
}