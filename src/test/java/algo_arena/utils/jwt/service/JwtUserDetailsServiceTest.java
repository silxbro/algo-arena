package algo_arena.utils.jwt.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import algo_arena.member.entity.Member;
import algo_arena.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

@SpringBootTest
class JwtUserDetailsServiceTest {

    @InjectMocks
    JwtUserDetailsService jwtUserDetailsService;

    @Mock
    MemberService memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("닉네임을 기준으로 유저 정보를 정상적으로 로드한다")
    void loadUserByUsername_Success() {
        //given
        String nickname = "jwt member";
        String password = "jwt!!!";
        Member member = Member.builder().name(nickname).password(password).build();
        when(memberService.findMemberByName(nickname)).thenReturn(member);

        //when
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(nickname);

        //then
        assertThat(userDetails.getUsername()).isEqualTo(nickname);
        assertThat(userDetails.getPassword()).isEqualTo(password);
        verify(memberService).findMemberByName(nickname);
    }
}