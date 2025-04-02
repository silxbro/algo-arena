package algo_arena.member.service;

import static algo_arena.common.exception.enums.ErrorType.*;

import algo_arena.member.entity.Member;
import algo_arena.member.exception.AuthException;
import algo_arena.member.exception.MemberException;
import algo_arena.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Member> findMembersByName(String name) {
        if (!StringUtils.hasText(name)) {
            return memberRepository.findAll();
        }
        return memberRepository.findAllByName(name);
    }

    public Member findMemberById(Long id) {
        if (id == null) {
            throw new MemberException(NULL_VALUE);
        }
        return memberRepository.findById(id)
            .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    public Member findMemberByName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new MemberException(NULL_VALUE);
        }
        return memberRepository.findByName(name)
            .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    @Transactional
    public void changePassword(String name, String password, String newPassword, String confirmNewPassword) {
        Member member = findMemberByName(name);

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new AuthException(PASSWORD_MISMATCH);
        }

        if (!newPassword.equals(confirmNewPassword)) {
            throw new AuthException(CONFIRM_PASSWORD_MISMATCH);
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        member.changePassword(encodedPassword);
    }

    @Transactional
    public void delete(Long id) {
        memberRepository.deleteById(id);
    }
}