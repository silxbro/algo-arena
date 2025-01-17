package algo_arena.problem.service;

import static algo_arena.member.enums.Role.ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import algo_arena.member.entity.Member;
import algo_arena.member.repository.MemberRepository;
import algo_arena.problem.entity.Problem;
import algo_arena.problem.repository.ProblemRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProblemServiceTest {

    @Autowired
    ProblemService problemService;

    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    MemberRepository memberRepository;

    Member adminMember;
    Member regularMember;

    Problem problem;

    @BeforeEach
    void setUp() {
        adminMember = Member.builder().name("adminMember").role(ADMIN).build();
        regularMember = Member.builder().name("regularMember").build();

        memberRepository.save(adminMember);
        memberRepository.save(regularMember);

        problem = createProblem(1L, "testProb", "testLink");
    }

    @Test
    @DisplayName("관리자가 문제를 성공적으로 등록할 수 있다")
    void registerProblem_Admin_Success() {
        //given
        Long problemNumber = problem.getNumber();

        //when
        Problem registeredProblem = problemService.registerProblem(problem, adminMember.getName());

        //then
        assertThat(registeredProblem).isNotNull();
        assertThat(problemRepository.findByNumber(problemNumber).orElse(null)).isEqualTo(problem);
    }

    @Test
    @DisplayName("일반 사용자가 문제를 등록하려고 할 때, 등록이 실패하고 예외가 발생한다")
    void registerProblem_User_Fail() {
        //given

        //when

        //then
        assertThatThrownBy(() -> problemService.registerProblem(problem, regularMember.getName()))
            .isInstanceOf(RuntimeException.class);
        assertThat(problemRepository.findByNumber(problem.getNumber())).isEmpty();
    }

    @Test
    @DisplayName("문제 번호로 문제를 조회할 수 있다")
    void findProblemByNumber() {
        //given
        problemRepository.save(problem);

        //when
        Problem findProblem = problemService.findProblemByNumber(problem.getNumber());

        //then
        assertThat(findProblem).isNotNull();
        assertThat(findProblem).isEqualTo(problem);
    }

    @Test
    @DisplayName("문제 번호의 범위 및 제목(대소문자 구분없이 포함) 조건을 만족하는 문제 목록을 조회할 수 있다")
    void findProblemsBySearch() {
        //given
        Problem problem1 = problemRepository.save(
            createProblem(12862L, "Love Triangles", "https://www.acmicpc.net/problem/12862"));
        Problem problem2 = problemRepository.save(
            createProblem(10718L, "We love kriii", "https://www.acmicpc.net/problem/10718"));
        Problem problem3 = problemRepository.save(
            createProblem(3385L, "Speed Limits", "https://www.acmicpc.net/problem/3385"));

        //when
        List<Problem> problems = problemService.findProblemsBySearch(
            null, 20000L, "love");

        //then
        assertThat(problems).containsExactly(problem1, problem2);
    }

    @Test
    @DisplayName("관리자가 문제를 성공적으로 수정할 수 있다")
    void updateProblem() {
        //given
        problemRepository.save(problem);
        String newTitle = "newTitle";
        String newLink = "newLink";

        //when
        problemService.updateProblem(problem.getNumber(), newTitle, newLink, adminMember.getName());
        Problem updatedProblem = problemRepository.findByNumber(problem.getNumber()).orElse(null);

        //then
        assertThat(updatedProblem).isNotNull();
        assertThat(updatedProblem.getTitle()).isEqualTo(newTitle);
        assertThat(updatedProblem.getLink()).isEqualTo(newLink);
    }

    @Test
    @DisplayName("관리자가 문제를 성공적으로 삭제할 수 있다")
    void deleteProblem() {
        //given
        problemRepository.save(problem);

        //when
        problemService.deleteProblem(problem.getNumber(), adminMember.getName());

        //then
        assertThat(problemRepository.findByNumber(problem.getNumber())).isEmpty();
    }

    private Problem createProblem(Long number, String title, String link) {
        return Problem.builder().number(number).title(title).link(link).build();
    }
}