package algo_arena.problem.repository;

import static org.assertj.core.api.Assertions.assertThat;

import algo_arena.problem.entity.Problem;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProblemRepositoryTest {

    @Autowired
    ProblemRepository problemRepository;

    Problem problem;

    @BeforeEach
    void setUp() {
        problem = Problem.builder()
            .number(9999L)
            .title("테스트 문제")
            .link("https://www.acmicpc.net/problem/9999")
            .build();
        problemRepository.save(problem);
    }

    @Test
    @DisplayName("문제 번호로 문제를 조회할 수 있다")
    void findByNumber() {
        //given
        Long problemNumber = problem.getNumber();
        String problemTitle = problem.getTitle();

        //when
        Optional<Problem> findProblem = problemRepository.findByNumber(problemNumber);

        //then
        assertThat(findProblem).isPresent();
        assertThat(findProblem.get().getTitle()).isEqualTo(problemTitle);
    }

    @Test
    @DisplayName("문제 번호로 문제를 삭제할 수 있다")
    void deleteByNumber() {
        //given
        Long problemNumber = problem.getNumber();

        //when
        problemRepository.deleteByNumber(problemNumber);
        Optional<Problem> deleteProblem = problemRepository.findByNumber(problemNumber);

        //then
        assertThat(deleteProblem).isEmpty();
    }
}