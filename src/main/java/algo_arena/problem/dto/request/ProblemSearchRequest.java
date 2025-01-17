package algo_arena.problem.dto.request;

import lombok.Getter;

@Getter
public class ProblemSearchRequest {

    private Long minNumber;
    private Long maxNumber;
    private String title;

}