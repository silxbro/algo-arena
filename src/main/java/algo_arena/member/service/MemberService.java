package algo_arena.member.service;

import algo_arena.member.entity.Member;
import algo_arena.member.repository.MemberRepository;
import algo_arena.utils.auth.service.CodeAuthService;
import algo_arena.utils.jwt.service.JwtTokenUtil;
import algo_arena.utils.jwt.service.JwtUserDetailsService;
import algo_arena.utils.mail.MailService;
import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final CodeAuthService codeAuthService;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    @Transactional
    public Member register(Member member) {
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.changePassword(encodedPassword);
        return memberRepository.save(member);
    }

    public List<Member> findMembersByNickname(String nickname) {
        return memberRepository.findAllByNickname(nickname);
    }

    public Member findMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow();
    }

    public Member findMemberByNickname(String nickname) {
        return memberRepository.findByNickname(nickname).orElseThrow();
    }

    public String authenticate(String email, String password) {
        final Member member = findMemberByEmail(email);
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(member.getNickname());
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalStateException("패스워드가 일치하지 않습니다.");
        }
        return jwtTokenUtil.generateToken(userDetails);
    }

    @Transactional
    public void sendAuthEmail(String email) {
        String authCode = generateAuthCode();
        codeAuthService.setAuthCodeAndExpiration(email, authCode, authCodeExpirationMillis);
        String emailTitle = "[Algo-Arena] 이메일 인증코드 발송 메일입니다.";
        String emailContent = "인증 코드: " + authCode;
        try {
            mailService.sendEmail(email, emailTitle, emailContent);
        } catch (MessagingException e) {

        } catch (UnsupportedEncodingException e) {

        }
    }

    @Transactional
    public void verifyAuthCode(String email, String enteredCode) {
        String code = codeAuthService.getAuthCode(email);
        if (code == null) {
            throw new IllegalStateException("이메일 인증을 먼저 시도해 주세요.");
        }
        if (!code.equals(enteredCode)) {
            throw new IllegalStateException("이메일 인증코드가 일치하지 않습니다.");
        }
        codeAuthService.markAuthAsCompleted(email);
    }

    @Transactional
    public Member updateImage(Long id, String imgUrl) {
        Member member = memberRepository.findById(id).orElseThrow();
        member.changeImage(imgUrl);
        return member;
    }

    @Transactional
    public Member changePassword(Long id, String password) {
        Member member = memberRepository.findById(id).orElseThrow();
        String encodedPassword = passwordEncoder.encode(password);
        member.changePassword(encodedPassword);
        return member;
    }

    @Transactional
    public void delete(Long id) {
        memberRepository.deleteById(id);
    }

    private String generateAuthCode() {
        Random random = new Random();
        int authCode = 100000 + random.nextInt(900000);  // 100000~999999 사이의 숫자
        return String.valueOf(authCode);
    }
}