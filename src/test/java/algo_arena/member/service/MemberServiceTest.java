package algo_arena.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import algo_arena.domain.member.entity.Member;
import algo_arena.domain.member.repository.MemberRepository;
import algo_arena.domain.member.service.MemberService;
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
    String name = "tester";
    String password = "password";

    @BeforeEach
    void setUp() {
        member = Member.builder()
            .email(email)
            .name(name)
            .password(passwordEncoder.encode(password))
            .build();
        memberRepository.save(member);
    }

    @Test
    @DisplayName("닉네임으로 회원 검색 시, 대소문자를 구분하지 않고 해당 닉네임을 포함하는 모든 회원을 검색할 수 있다")
    void findMembersByName() {
        //given
        String findName = "test";

        //when
        List<Member> members = memberService.findMembersByName(findName);

        //then
        assertThat(members).containsExactly(member);
    }

    @Test
    @DisplayName("기존 비밀번호 및 확인 비밀번호가 올바를 경우, 회원의 비밀번호를 변경할 수 있다")
    void changePassword() {
        //given
        String newPassword = "newPassword";

        //when
        memberService.changePassword(member.getName(), password, newPassword, newPassword);
        Member updatedMember = memberService.findMemberById(member.getId());

        //then
        assertThat(passwordEncoder.matches(newPassword, updatedMember.getPassword())).isTrue();
    }

    @Test
    @DisplayName("기존 비밀번호가 올바르지 않을 경우, 회원의 비밀번호를 변경할 수 없고 예외가 발생한다")
    void changePassword_wrongPassword() {
        //given
        String wrongPassword = "wrongPassword";
        String newPassword = "newPassword";

        //when

        //then
        assertThatThrownBy(() -> memberService.changePassword(member.getName(), wrongPassword, newPassword, newPassword))
            .isInstanceOf(RuntimeException.class);

        String memberPassword = memberService.findMemberById(member.getId()).getPassword();
        assertThat(passwordEncoder.matches(newPassword, memberPassword)).isFalse();
    }

    @Test
    @DisplayName("변경 확인용 비밀번호가 올바르지 않을 경우, 회원의 비밀번호를 변경할 수 없고 예외가 발생한다")
    void changePassword_wrongConfirmPassword() {
        //given
        String newPassword = "newPassword";
        String wrongConfirmPassword = "wrongPassword";

        //when

        //then
        assertThatThrownBy(() -> memberService.changePassword(member.getName(), password, newPassword, wrongConfirmPassword))
            .isInstanceOf(RuntimeException.class);

        String memberPassword = memberService.findMemberById(member.getId()).getPassword();
        assertThat(passwordEncoder.matches(newPassword, memberPassword)).isFalse();
    }
}