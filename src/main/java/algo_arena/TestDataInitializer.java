package algo_arena;

import algo_arena.member.entity.Member;
import algo_arena.member.repository.MemberRepository;
import algo_arena.problem.entity.Problem;
import algo_arena.problem.repository.ProblemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class TestDataInitializer {

    private final MemberRepository memberRepository;
    private final ProblemRepository problemRepository;

    @PostConstruct
    public void init() {
        Member member1 = Member.builder().nickname("member 1").build();
        Member member2 = Member.builder().nickname("member 2").build();

        Problem problem1 = Problem.builder().title("problem 1").build();
        Problem problem2 = Problem.builder().title("problem 2").build();
        Problem problem3 = Problem.builder().title("problem 3").build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        problemRepository.save(problem1);
        problemRepository.save(problem2);
        problemRepository.save(problem3);
    }
}
