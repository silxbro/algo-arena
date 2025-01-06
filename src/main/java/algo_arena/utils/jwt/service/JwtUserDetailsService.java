package algo_arena.utils.jwt.service;

import algo_arena.member.entity.Member;
import algo_arena.member.repository.MemberRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        Member member = memberRepository.findByNickname(nickname).orElseThrow();
        return new User(nickname, member.getPassword(), new ArrayList<>());
    }
}