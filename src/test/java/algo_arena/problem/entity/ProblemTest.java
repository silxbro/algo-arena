package algo_arena.problem.entity;

import static org.assertj.core.api.Assertions.assertThat;

import algo_arena.domain.problem.entity.Problem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProblemTest {

    Problem problem;
    Long number = 1041L;
    String title = "주사위";
    String link = "https://www.acmicpc.net/problem/1041";

    @BeforeEach
    void setUp() {
        problem = Problem.builder()
            .number(number)
            .title(title)
            .link(link)
            .build();
    }

    @Test
    @DisplayName("문제의 제목을 변경할 수 있다")
    void changeTitle() {
        //given
        String newTitle = "내멋대로 주사위";

        //when
        problem.changeTitle(newTitle);

        //then
        assertThat(problem.getTitle()).isEqualTo(newTitle);
    }

    @Test
    @DisplayName("문제의 링크를 변경할 수 있다")
    void changeLink() {
        //given
        String newLink = link + "editVersion";

        //when
        problem.changeLink(newLink);

        //then
        assertThat(problem.getLink()).isEqualTo(newLink);
    }
}