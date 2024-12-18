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

    private Member createMember(String nickname) {
        return Member.builder().nickname(nickname).build();
    }

    @Test
    @DisplayName("새로운 회원 정보를 저장할 경우, 해당 정보가 정확히 저장된다")
    void save() {
        //given
        Member member = createMember("myMember");
        Member savedMember = memberRepository.save(member);

        //when

        //then
        assertThat(savedMember).isEqualTo(member);
    }

    @Test
    @DisplayName("모든 회원을 조회할 수 있다")
    void findAll() {
        //given
        Member member1 = createMember("member1");
        Member member2 = createMember("member2");
        Member member3 = createMember("member3");

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
        Member member = createMember("idMember");
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
        Member member1 = createMember("member1");
        Member member2 = createMember("member2");
        Member member3 = createMember("member3");

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        memberRepository.deleteById(member2.getId());

        //when
        List<Member> members = memberRepository.findAll();

        //then
        assertThatThrownBy(() -> memberRepository.findById(member2.getId()).get())
            .isInstanceOf(NoSuchElementException.class);
        assertThat(members).containsExactly(member1, member3);
    }

    @Test
    @DisplayName("회원을 닉네임으로 조회할 때, 대소문자를 구분하지 않고 해당 닉네임을 포함하는 모든 회원이 조회되어야 한다")
    void findByNickname() {
        //given
        Member james = createMember("James");
        Member jane = createMember("Jane");
        Member benjamin = createMember("Benjamin");

        memberRepository.save(james);
        memberRepository.save(jane);
        memberRepository.save(benjamin);

        //when
        List<Member> members = memberRepository.findByNickname("jam");

        //then
        assertThat(members).containsExactly(james, benjamin);
    }
}