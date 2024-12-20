package algo_arena.submission.repository;

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
class SubmissionRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    SubmissionRepository submissionRepository;

    Submission submission1, submission2, submission3;

    @BeforeEach
    void setUp() {
        Problem problem1 = Problem.builder().number(10).build();
        Problem problem2 = Problem.builder().number(20).build();

        problemRepository.save(problem1);
        problemRepository.save(problem2);

        Member member1 = Member.builder().nickname("member1").build();
        Member member2 = Member.builder().nickname("member2").build();

        memberRepository.save(member1);
        memberRepository.save(member2);

        submission1 = createSubmission(problem1, member1, Language.JAVA, JudgementResult.RIGHT);
        submission2 = createSubmission(problem1, member2, Language.PYTHON, JudgementResult.COMPILE_ERROR);
        submission3 = createSubmission(problem2, member1, Language.JAVA, JudgementResult.TIME_EXCEEDED);

        submissionRepository.save(submission1);
        submissionRepository.save(submission2);
        submissionRepository.save(submission3);
    }

    @Test
    @DisplayName("검색 조건에 맞는 제출 목록을 조회할 수 있다")
    void findSubmission_SearchCondition() {
        //given
        SubmissionSearchCond searchCond1 = SubmissionSearchCond.builder()
            .memberNickname("member1")
            .languageName("Java")
            .build();
        SubmissionSearchCond searchCond2 = SubmissionSearchCond.builder()
            .problemNumber(10)
            .resultDescription("컴파일 오류")
            .build();

        //when
        List<Submission> submissions1 = submissionRepository.findSubmissions(searchCond1);
        List<Submission> submissions2 = submissionRepository.findSubmissions(searchCond2);
        List<Submission> all = submissionRepository.findSubmissions(SubmissionSearchCond.builder().build());

        //then
        assertThat(submissions1).containsExactly(submission1, submission3);
        assertThat(submissions2).containsExactly(submission2);
        assertThat(all).containsExactly(submission1, submission2, submission3);
    }

    private Submission createSubmission(Problem problem, Member member, Language language, JudgementResult result) {
        return Submission.builder()
            .problem(problem)
            .member(member)
            .language(language)
            .result(result)
            .build();
    }

}