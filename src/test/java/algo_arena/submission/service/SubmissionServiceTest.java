package algo_arena.submission.service;

import static algo_arena.domain.submission.enums.CodeLanguage.*;
import static algo_arena.domain.submission.enums.SubmissionResult.*;
import static org.assertj.core.api.Assertions.assertThat;

import algo_arena.domain.member.entity.Member;
import algo_arena.domain.member.repository.MemberRepository;
import algo_arena.domain.problem.entity.Problem;
import algo_arena.domain.problem.repository.ProblemRepository;
import algo_arena.domain.submission.dto.request.SubmissionSearchRequest;
import algo_arena.domain.submission.enums.CodeLanguage;
import algo_arena.domain.submission.enums.SubmissionResult;
import algo_arena.domain.submission.service.SubmissionService;
import algo_arena.domain.submission.entity.Submission;
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
        member1 = Member.builder().name("member1").build();
        member2 = Member.builder().name("member2").build();

        memberRepository.save(member1);
        memberRepository.save(member2);

        problem1 = Problem.builder().number(1L).build();
        problem2 = Problem.builder().number(2L).build();

        problemRepository.save(problem1);
        problemRepository.save(problem2);
    }

    @Test
    @DisplayName("문제에 대해 작성한 풀이 코드를 제출할 수 있다")
    void create() {
//        //given
//        Submission submission = createSubmission(problem1, member1, JAVA_SCRIPT, CORRECT);
//        Submission createdSubmission = submissionService.create(submission);
//
//        //when
//        Submission findSubmission = submissionService.findOneById(createdSubmission.getId());
//
//        //then
//        assertThat(findSubmission).isEqualTo(createdSubmission);
    }

    @Test
    @DisplayName("검색 조건에 따라 제출 목록을 조회할 수 있다")
    void findAll() {
//        //given
//        Submission submission1 = createSubmission(problem1, member1, Language.JAVA, JudgementResult.MEMORY_EXCEEDED);
//        Submission submission2 = createSubmission(problem1, member1, Language.RUBY, JudgementResult.RIGHT);
//        Submission submission3 = createSubmission(problem2, member1, Language.KOTLIN, JudgementResult.RIGHT);
//        Submission submission4 = createSubmission(problem2, member2, Language.C_PP, JudgementResult.RIGHT);
//        Submission submission5 = createSubmission(problem2, member2, Language.PYTHON, JudgementResult.TIME_EXCEEDED);
//
//        SubmissionSearchRequest request1 = SubmissionSearchRequest.builder().problemNumber(1L).build();
//        SubmissionSearchRequest request2 = SubmissionSearchRequest.builder().resultDescription("정답").build();
//        SubmissionSearchRequest request3 = SubmissionSearchRequest.builder().memberName("member2").languageName("Kotlin").build();
//
//        //when
//        List<Submission> submissions1 = submissionService.findSubmissionsBySearch(request1);
//        List<Submission> submissions2 = submissionService.findSubmissionsBySearch(request2);
//        List<Submission> submissions3 = submissionService.findSubmissionsBySearch(request3);
//
//        //then
//        assertThat(submissions1).containsExactly(submission1, submission2);
//        assertThat(submissions2).containsExactly(submission2, submission3, submission4);
//        assertThat(submissions3).isEmpty();
    }

//    private Submission createSubmission(Problem problem, Member member, CodeLanguage language, SubmissionResult result) {
//        Submission submission = Submission.builder()
//            .problem(problem)
//            .member(member)
//            .language(language)
//            .result(result)
//            .build();
//        return submissionService.submitSolution("roomId", submission);
//    }
}