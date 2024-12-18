package algo_arena.member.service;

import algo_arena.member.entity.Member;
import algo_arena.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member create(Member member) {
        return memberRepository.save(member);
    }

    public List<Member> findAllByNickname(String nickname) {
        return memberRepository.findByNickname(nickname);
    }

    public Member findOneById(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void updateImage(Long id, String imgUrl) {
        Member member = memberRepository.findById(id).orElseThrow();
        member.changeImage(imgUrl);
    }

    @Transactional
    public void changePassword(Long id, String password) {
        Member member = memberRepository.findById(id).orElseThrow();
        member.changePassword(password);
    }

    @Transactional
    public void delete(Long id) {
        memberRepository.deleteById(id);
    }
}