package algo_arena.utils.jwt.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import algo_arena.member.entity.Member;
import algo_arena.member.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class JwtUserDetailsServiceTest {

    @InjectMocks
    JwtUserDetailsService jwtUserDetailsService;

    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("닉네임을 기준으로 유저 정보를 정상적으로 로드한다")
    void loadUserByUsername_Success() {
        //given
        String name = "jwt member";
        String password = "jwt!!!";
        Member member = createMember(name, password);
        when(memberRepository.findByName(name)).thenReturn(Optional.of(member));

        //when
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(name);

        //then
        assertThat(userDetails.getUsername()).isEqualTo(name);
        assertThat(userDetails.getPassword()).isEqualTo(password);
        verify(memberRepository).findByName(name);
    }

    private Member createMember(String name, String password) {
        return Member.builder().name(name).password(password).build();
    }
}