package algo_arena.problem.dto.response;

import algo_arena.problem.entity.Problem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProblemRegisterResponse {

    private Long problemId;
    private Long number;

    public static ProblemRegisterResponse from(Problem problem) {
        return ProblemRegisterResponse.builder()
            .problemId(problem.getId())
            .number(problem.getNumber())
            .build();
    }
}