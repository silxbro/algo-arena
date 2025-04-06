package algo_arena.domain.problem.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class ProblemSearchRequest {

    @Positive(message = "문제 번호는 자연수이어야 합니다.")
    private Long minNumber;

    @Positive(message = "문제 번호는 자연수이어야 합니다.")
    private Long maxNumber;

    private String title;

}