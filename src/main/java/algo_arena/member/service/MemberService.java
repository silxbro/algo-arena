package algo_arena.member.service;

import algo_arena.member.entity.Member;
import algo_arena.member.repository.MemberRepository;
import algo_arena.utils.jwt.service.JwtTokenUtil;
import algo_arena.utils.jwt.service.JwtUserDetailsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public Member saveMember(Member member) {
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
            throw new IllegalStateException();
        }
        return jwtTokenUtil.generateToken(userDetails);
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
        member.changePassword(password);
        return member;
    }

    @Transactional
    public void delete(Long id) {
        memberRepository.deleteById(id);
    }
}