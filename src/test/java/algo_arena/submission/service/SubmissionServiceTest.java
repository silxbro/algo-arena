package algo_arena.submission.service;

import static org.assertj.core.api.Assertions.assertThat;

import algo_arena.member.entity.Member;
import algo_arena.member.repository.MemberRepository;
import algo_arena.problem.entity.Problem;
import algo_arena.problem.repository.ProblemRepository;
import algo_arena.submission.dto.request.SubmissionSearchCond;
import algo_arena.submission.entity.JudgementResult;
import algo_arena.submission.entity.Language;
import algo_arena.submission.entity.Submission;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class SubmissionServiceTest {

    @Autowired
    SubmissionService submissionService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProblemRepository problemRepository;

    Member member1, member2;
    Problem problem1, problem2;

    @BeforeEach
    void setUP() {
        member1 = Member.builder().nickname("member1").build();
        member2 = Member.builder().nickname("member2").build();

        memberRepository.save(member1);
        memberRepository.save(member2);

        problem1 = Problem.builder().number(1).build();
        problem2 = Problem.builder().number(2).build();

        problemRepository.save(problem1);
        problemRepository.save(problem2);
    }

    @Test
    @DisplayName("문제에 대해 작성한 풀이 코드를 제출할 수 있다")
    void create() {
        //given
        Submission submission = createSubmission(problem1, member1, Language.JAVA_SCRIPT, JudgementResult.RIGHT);
        Submission createdSubmission = submissionService.create(submission);

        //when
        Submission findSubmission = submissionService.findOneById(createdSubmission.getId());

        //then
        assertThat(findSubmission).isEqualTo(createdSubmission);
    }

    @Test
    @DisplayName("검색 조건에 따라 제출 목록을 조회할 수 있다")
    void findAll() {
        //given
        Submission submission1 = createSubmission(problem1, member1, Language.JAVA, JudgementResult.MEMORY_EXCEEDED);
        Submission submission2 = createSubmission(problem1, member1, Language.RUBY, JudgementResult.RIGHT);
        Submission submission3 = createSubmission(problem2, member1, Language.KOTLIN, JudgementResult.RIGHT);
        Submission submission4 = createSubmission(problem2, member2, Language.C_PP, JudgementResult.RIGHT);
        Submission submission5 = createSubmission(problem2, member2, Language.PYTHON, JudgementResult.TIME_EXCEEDED);

        SubmissionSearchCond searchCond1 = SubmissionSearchCond.builder().problemNumber(1).build();
        SubmissionSearchCond searchCond2 = SubmissionSearchCond.builder().resultDescription("정답").build();
        SubmissionSearchCond searchCond3 = SubmissionSearchCond.builder().memberNickname("member2").languageName("Kotlin").build();

        //when
        List<Submission> submissions1 = submissionService.findAll(searchCond1);
        List<Submission> submissions2 = submissionService.findAll(searchCond2);
        List<Submission> submissions3 = submissionService.findAll(searchCond3);

        //then
        assertThat(submissions1).containsExactly(submission1, submission2);
        assertThat(submissions2).containsExactly(submission2, submission3, submission4);
        assertThat(submissions3).isEmpty();
    }

    private Submission createSubmission(Problem problem, Member member, Language language, JudgementResult result) {
        Submission submission = Submission.builder()
            .problem(problem)
            .member(member)
            .language(language)
            .result(result)
            .build();
        return submissionService.create(submission);
    }
}