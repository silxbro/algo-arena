package algo_arena.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import algo_arena.member.entity.Member;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
            .email("bella@google.com")
            .nickname("bella_Swan")
            .password("bella!!!")
            .imgUrl("https://my-image.com/bella.png")
            .build();
        memberService.register(member);
    }

    @Test
    @DisplayName("회원 가입을 할 수 있다")
    void register() {
        //given
        Member newMember = Member.builder()
            .email("new@google.com")
            .nickname("new")
            .password("new!!!")
            .imgUrl("https://my-image.com/new.png")
            .build();

        //when
        Member registeredMember = memberService.register(newMember);

        //then
        assertThat(registeredMember).isEqualTo(newMember);
    }

    @Test
    @DisplayName("닉네임으로 회원 검색 시, 대소문자를 구분하지 않고 해당 닉네임을 포함하는 모든 회원을 검색할 수 있다")
    void findAllByNickname() {
        //given

        //when
        List<Member> members = memberService.findMembersByNickname("swan");

        //then
        assertThat(members).containsExactly(member);
    }

    @Test
    @DisplayName("로그인 정보가 올바를 때, 토큰이 발급된다")
    void authenticate_Success() {
        //given
        String rightPassword = "bella!!!";

        //when
        String token = memberService.authenticate(member.getEmail(), rightPassword);

        //then
        assertThat(token).isNotNull();
    }

    @Test
    @DisplayName("로그인 정보가 올바르지 않을 때, 예외가 발생한다")
    void authenticate_Fail() {
        //given
        String wrongPassword = "wrong password";

        //when

        //then
        assertThatThrownBy(() -> memberService.authenticate(member.getEmail(), wrongPassword))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("회원의 이미지를 변경할 수 있다")
    void updateImage() {
        //given
        String imageUrl = "https://update-image.com/bella.png";
        memberService.updateImage(member.getId(), imageUrl);

        //when
        Member findMember = memberService.findMemberById(member.getId());

        //then
        assertThat(findMember.getImgUrl()).isEqualTo(imageUrl);
    }

    @Test
    @DisplayName("회원의 비밀번호를 변경할 수 있다")
    void changePassword() {
        //given
        String password = "!!!bella";
        memberService.changePassword(member.getId(), password);

        //when
        Member findMember = memberService.findMemberById(member.getId());

        //then
        assertThat(passwordEncoder.matches(password, findMember.getPassword())).isTrue();
    }
}