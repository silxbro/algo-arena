package algo_arena.problem.dto.response;

import algo_arena.problem.entity.Problem;
import java.util.Collection;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProblemListResponse {

    private long number;
    private List<ProblemBasicResponse> problemList;

    public static ProblemListResponse from(Collection<Problem> problems) {
        return ProblemListResponse.builder()
            .number(problems.size())
            .problemList(problems.stream().map(ProblemBasicResponse::from).toList())
            .build();
    }
}