package algo_arena;

import algo_arena.member.entity.Member;
import algo_arena.member.repository.MemberRepository;
import algo_arena.problem.entity.Problem;
import algo_arena.problem.repository.ProblemRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestDataInitializer {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProblemRepository problemRepository;

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
