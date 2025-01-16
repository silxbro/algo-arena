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

    public List<Member> findMembersByNickname(String nickname) {
        return memberRepository.findAllByNickname(nickname);
    }

    public Member findMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }

    public Member findMemberByNickname(String nickname) {
        return memberRepository.findByNickname(nickname).orElseThrow();
    }

    @Transactional
    public Member changePassword(String nickname, String password) {
        Member member = memberRepository.findByNickname(nickname).orElseThrow();
        String encodedPassword = passwordEncoder.encode(password);
        member.changePassword(encodedPassword);
        return member;
    }

    @Transactional
    public void delete(Long id) {
        memberRepository.deleteById(id);
    }
}