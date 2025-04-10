package algo_arena.submission.repository;

import static org.assertj.core.api.Assertions.assertThat;

import algo_arena.domain.member.entity.Member;
import algo_arena.domain.member.repository.MemberRepository;
import algo_arena.domain.problem.entity.Problem;
import algo_arena.domain.problem.repository.ProblemRepository;
import algo_arena.domain.submission.dto.request.SubmissionSearchRequest;
import algo_arena.domain.submission.enums.CodeLanguage;
import algo_arena.domain.submission.enums.SubmissionResult;
import algo_arena.domain.submission.entity.Submission;
import algo_arena.domain.submission.repository.SubmissionRepository;
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
        Problem problem1 = Problem.builder().number(10L).build();
        Problem problem2 = Problem.builder().number(20L).build();

        problemRepository.save(problem1);
        problemRepository.save(problem2);

        Member member1 = Member.builder().name("member1").build();
        Member member2 = Member.builder().name("member2").build();

        memberRepository.save(member1);
        memberRepository.save(member2);

        submission1 = createSubmission(problem1, member1, CodeLanguage.JAVA, SubmissionResult.CORRECT);
        submission2 = createSubmission(problem1, member2, CodeLanguage.PYTHON, SubmissionResult.COMPILE_ERROR);
        submission3 = createSubmission(problem2, member1, CodeLanguage.JAVA, SubmissionResult.TIME_EXCEEDED);

        submissionRepository.save(submission1);
        submissionRepository.save(submission2);
        submissionRepository.save(submission3);
    }

    @Test
    @DisplayName("검색 조건에 맞는 제출 목록을 조회할 수 있다")
    void findSubmission_SearchCondition() {
        //given
        SubmissionSearchRequest request1 = SubmissionSearchRequest.builder()
            .memberName("member1")
            .languageName("Java")
            .build();
        SubmissionSearchRequest request2 = SubmissionSearchRequest.builder()
            .problemNumber(10L)
            .resultDescription("컴파일 오류")
            .build();

        //when
        List<Submission> submissions1 = submissionRepository.findSubmissions(request1);
        List<Submission> submissions2 = submissionRepository.findSubmissions(request2);
        List<Submission> all = submissionRepository.findSubmissions(SubmissionSearchRequest.builder().build());

        //then
        assertThat(submissions1).containsExactly(submission1, submission3);
        assertThat(submissions2).containsExactly(submission2);
        assertThat(all).containsExactly(submission1, submission2, submission3);
    }

    private Submission createSubmission(Problem problem, Member member, CodeLanguage language, SubmissionResult result) {
        return Submission.builder()
            .problem(problem)
            .member(member)
            .language(language)
            .result(result)
            .build();
    }
}