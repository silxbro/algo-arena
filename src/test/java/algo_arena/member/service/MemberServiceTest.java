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
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
            .email("bella@google.com")
            .nickname("bella_Swan")
            .password("bella!!!")
            .imgUrl("https://my-image.com/bella.png")
            .build();
        memberRepository.save(member);
    }

    @Test
    @DisplayName("회원 가입을 할 수 있다")
    void create() {
        //given
        Member member = Member.builder().nickname("hello").build();
        Member createdMember = memberService.saveMember(member);

        //when
        Member findMember = memberService.findMemberById(createdMember.getId());

        //then
        assertThat(findMember).isEqualTo(member);
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
    @DisplayName("회원의 이미지를 변경할 수 있다")
    void updateImage() {
        //given
        memberService.updateImage(member.getId(), "https://update-image.com/bella.png");

        //when
        Member findMember = memberService.findMemberById(member.getId());

        //then
        assertThat(findMember.getImgUrl()).isEqualTo("https://update-image.com/bella.png");
    }

    @Test
    @DisplayName("회원의 비밀번호를 변경할 수 있다")
    void changePassword() {
        //given
        memberService.changePassword(member.getId(), "!!!bella");

        //when
        Member findMember = memberService.findMemberById(member.getId());

        //then
        assertThat(findMember.getPassword()).isEqualTo("!!!bella");
    }
}