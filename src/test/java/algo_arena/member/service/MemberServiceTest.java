package algo_arena.member.service;

import static org.assertj.core.api.Assertions.assertThat;

import algo_arena.member.entity.Member;
import algo_arena.member.repository.MemberRepository;
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
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    Member member;

    String email = "test@example.com";
    String nickname = "tester";
    String password = "password";
    String imgUrl = "https://my-image.com/origin-image.png";

    @BeforeEach
    void setUp() {
        member = Member.builder()
            .email(email)
            .nickname(nickname)
            .password(passwordEncoder.encode(password))
            .imgUrl(imgUrl)
            .build();
        memberRepository.save(member);
    }

    @Test
    @DisplayName("닉네임으로 회원 검색 시, 대소문자를 구분하지 않고 해당 닉네임을 포함하는 모든 회원을 검색할 수 있다")
    void findMembersByNickname() {
        //given
        String findNickname = "test";

        //when
        List<Member> members = memberService.findMembersByNickname(findNickname);

        //then
        assertThat(members).containsExactly(member);
    }

    @Test
    @DisplayName("회원의 프로필 이미지를 변경할 수 있다")
    void updateImage() {
        //given
        String newImgUrl = "https://my-image.com/new-image.png";

        //when
        memberService.updateImage(member.getId(), newImgUrl);
        Member updatedMember = memberService.findMemberById(member.getId());

        //then
        assertThat(updatedMember.getImgUrl()).isEqualTo(newImgUrl);
    }

    @Test
    @DisplayName("회원의 비밀번호를 변경할 수 있다")
    void changePassword() {
        //given
        String newPassword = "newPassword";

        //when
        memberService.changePassword(member.getId(), newPassword);
        Member updatedMember = memberService.findMemberById(member.getId());

        //then
        assertThat(passwordEncoder.matches(newPassword, updatedMember.getPassword())).isTrue();
    }
}