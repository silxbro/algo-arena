package algo_arena.member.service;

import algo_arena.member.entity.Member;
import algo_arena.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Member> findMembersByName(String name) {
        return memberRepository.findAllByName(name);
    }

    public Member findMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }

    public Member findMemberByName(String name) {
        return memberRepository.findByName(name).orElseThrow();
    }

    @Transactional
    public void changePassword(String name, String password, String newPassword, String confirmNewPassword) {
        Member member = memberRepository.findByName(name)
            .orElseThrow(() -> new RuntimeException("회원 정보가 존재하지 않습니다."));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new RuntimeException("현재 비밀번호가 일치하지 않습니다");
        }

        if (!newPassword.equals(confirmNewPassword)) {
            throw new RuntimeException("변경 확인 비밀번호가 일치하지 않습니다.");
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        member.changePassword(encodedPassword);
    }

    @Transactional
    public void delete(Long id) {
        memberRepository.deleteById(id);
    }
}