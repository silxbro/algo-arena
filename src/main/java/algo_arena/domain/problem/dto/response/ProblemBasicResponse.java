package algo_arena.domain.problem.dto.response;

import algo_arena.domain.problem.entity.Problem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProblemBasicResponse {

    private Long number;
    private String title;
    private String link;

    public static ProblemBasicResponse from(Problem problem) {
        return ProblemBasicResponse.builder()
            .number(problem.getNumber())
            .title(problem.getTitle())
            .link(problem.getLink())
            .build();
    }
}