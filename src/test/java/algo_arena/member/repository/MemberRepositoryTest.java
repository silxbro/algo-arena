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
    String name = "my-member";

    @Test
    @DisplayName("새로운 회원 정보를 저장할 경우, 해당 정보가 정확히 저장된다")
    void save() {
        //given
        Member member = createMember(email, name);

        //when
        Member savedMember = memberRepository.save(member);

        //then
        assertThat(savedMember.getEmail()).isEqualTo(email);
        assertThat(savedMember.getName()).isEqualTo(name);
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
    void findById_Found() {
        //given
        Member member = createMember(email, name);
        memberRepository.save(member);

        //when
        Member findMember = memberRepository.findById(member.getId()).orElse(null);

        //then
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    @DisplayName("존재하지 않는 회원 아이디로 회원 정보를 조회할 때 예외가 발생한다")
    void findById_NotFound() {
        //given
        Long notExistId = 1L;

        //when

        //then
        assertThatThrownBy(() -> memberRepository.findById(notExistId).orElseThrow())
            .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("회원 아이디로 해당 회원을 삭제할 수 있다")
    void deleteById() {
        //given
        Member member1 = createMember(email, "member1");
        Member member2 = createMember(email, "member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        memberRepository.deleteById(member2.getId());
        List<Member> members = memberRepository.findAll();

        //then
        assertThat(members).containsExactly(member1);
    }

    @Test
    @DisplayName("회원을 닉네임으로 조회할 때, 대소문자를 구분하지 않고 해당 닉네임을 포함하는 모든 회원이 조회되어야 한다")
    void findAllByName() {
        //given
        Member james = createMember(email, "James");
        Member jane = createMember(email, "Jane");
        Member benjamin = createMember(email, "Benjamin");

        memberRepository.save(james);
        memberRepository.save(jane);
        memberRepository.save(benjamin);

        //when
        List<Member> members = memberRepository.findAllByName("jam");

        //then
        assertThat(members).containsExactly(james, benjamin);
    }

    @Test
    @DisplayName("회원 닉네임으로 회원 정보를 조회할 수 있다")
    void findByName_Found() {
        //given
        Member member = createMember(email, name);
        memberRepository.save(member);

        //when
        Member findMember = memberRepository.findByName(name).orElseThrow();

        //then
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    @DisplayName("존재하지 않는 회원 닉네임으로 회원 정보를 조회할 때 예외가 발생한다")
    void findByNName_NotFound() {
        //given
        String notExistName = "not-exist-name";

        //when

        //then
        assertThatThrownBy(() -> memberRepository.findByName(notExistName).orElseThrow())
            .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("회원 이메일로 회원 정보를 조회할 수 있다")
    void findByEmail_Found() {
        //given
        Member member = createMember(email, name);
        memberRepository.save(member);

        //when
        Member findMember = memberRepository.findByEmail(email).orElseThrow();

        //then
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    @DisplayName("존재하지 않는 회원 이메일로 회원 정보를 조회할 때 예외가 발생한다")
    void findByEmail_NotFound() {
        //given
        String notExistEmail = "not-exist-email@example.com";

        //when

        //then
        assertThatThrownBy(() -> memberRepository.findByEmail(notExistEmail).orElseThrow())
            .isInstanceOf(NoSuchElementException.class);
    }

    private Member createMember(String email, String name) {
        return Member.builder().email(email).name(name).build();
    }
}