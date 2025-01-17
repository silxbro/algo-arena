package algo_arena.problem.dto.request;

import algo_arena.problem.entity.Problem;
import lombok.Getter;

@Getter
public class ProblemRegisterRequest {

    private Long number;
    private String title;
    private String link;

    public Problem toEntity() {
        return Problem.builder()
            .number(number)
            .title(title)
            .link(link)
            .build();
    }
}