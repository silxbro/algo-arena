package algo_arena.member.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import algo_arena.member.entity.Member;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    String email = "my-member@algoarena.com";
    String nickname = "my-member";

    @Test
    @DisplayName("새로운 회원 정보를 저장할 경우, 해당 정보가 정확히 저장된다")
    void save() {
        //given
        Member member = createMember(email, nickname);

        //when
        Member savedMember = memberRepository.save(member);

        //then
        assertThat(savedMember).isEqualTo(member);
    }

    @Test
    @DisplayName("모든 회원을 조회할 수 있다")
    void findAll() {
        //given
        Member member1 = createMember(email, "member1");
        Member member2 = createMember(email, "member2");
        Member member3 = createMember(email, "member3");

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        //when
        List<Member> members = memberRepository.findAll();

        //then
        assertThat(members).containsExactly(member1, member2, member3);
    }

    @Test
    @DisplayName("회원 아이디로 회원 정보를 조회할 수 있다")
    void findById() {
        //given
        Member member = createMember(email, nickname);
        memberRepository.save(member);

        //when
        Member findMember = memberRepository.findById(member.getId()).orElse(null);

        //then
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    @DisplayName("회원 아이디로 해당 회원을 삭제할 수 있다")
    void deleteById() {
        //given
        Member member1 = createMember(email, "member1");
        Member member2 = createMember(email, "member2");
        Member member3 = createMember(email, "member3");

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        //when
        memberRepository.deleteById(member2.getId());
        List<Member> members = memberRepository.findAll();

        //then
        assertThatThrownBy(() -> memberRepository.findById(member2.getId()).orElseThrow())
            .isInstanceOf(NoSuchElementException.class);
        assertThat(members).containsExactly(member1, member3);
    }

    @Test
    @DisplayName("회원을 닉네임으로 조회할 때, 대소문자를 구분하지 않고 해당 닉네임을 포함하는 모든 회원이 조회되어야 한다")
    void findAllByNickname() {
        //given
        Member james = createMember(email, "James");
        Member jane = createMember(email, "Jane");
        Member benjamin = createMember(email, "Benjamin");

        memberRepository.save(james);
        memberRepository.save(jane);
        memberRepository.save(benjamin);

        //when
        List<Member> members = memberRepository.findAllByNickname("jam");

        //then
        assertThat(members).containsExactly(james, benjamin);
    }

    @Test
    @DisplayName("회원 닉네임으로 회원 정보를 조회할 수 있다")
    void findByNickname() {
        //given
        Member member = createMember(email, nickname);
        memberRepository.save(member);

        //when
        Member findMember = memberRepository.findByNickname(nickname).orElseThrow();

        //then
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    @DisplayName("회원 이메일로 회원 정보를 조회할 수 있다")
    void findByEmail() {
        //given
        Member member = createMember(email, nickname);
        memberRepository.save(member);

        //when
        Member findMember = memberRepository.findByEmail(email).orElseThrow();

        //then
        assertThat(findMember).isEqualTo(member);
    }

    private Member createMember(String email, String nickname) {
        return Member.builder().email(email).nickname(nickname).build();
    }
}