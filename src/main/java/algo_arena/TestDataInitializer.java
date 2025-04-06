package algo_arena;

import static algo_arena.domain.member.enums.Role.ADMIN;

import algo_arena.domain.member.entity.Member;
import algo_arena.domain.member.repository.MemberRepository;
import algo_arena.domain.problem.entity.Problem;
import algo_arena.domain.problem.repository.ProblemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class TestDataInitializer {

    private final MemberRepository memberRepository;
    private final ProblemRepository problemRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        Member member1 = Member.builder().email("member1@gmail.com").name("member1").password(passwordEncoder.encode("member1")).build();
        Member member2 = Member.builder().email("member2@gmail.com").name("member2").password(passwordEncoder.encode("member2")).build();
        Member member3 = Member.builder().email("member3@gmail.com").name("member3").password(passwordEncoder.encode("member3")).build();
        Member admin = Member.builder().email("silbroeh@gmail.com").name("ADMIN").password(passwordEncoder.encode("admin")).role(ADMIN).build();

        Problem problem1 = Problem.builder().title("problem1").build();
        Problem problem2 = Problem.builder().title("problem2").build();
        Problem problem3 = Problem.builder().title("problem3").build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(admin);

        problemRepository.save(problem1);
        problemRepository.save(problem2);
        problemRepository.save(problem3);
    }
}
