package algo_arena.domain.problem.dto.request;

import algo_arena.domain.problem.entity.Problem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class ProblemRegisterRequest {

    @NotNull(message = "문제 번호를 입력해 주세요.")
    @Positive(message = "문제 번호는 자연수이어야 합니다.")
    private Long number;

    @NotBlank(message = "문제 제목을 입력해 주세요.")
    private String title;

    @NotBlank(message = "문제 페이지 URL 을 입력해 주세요.")
    private String url;

    public Problem toEntity() {
        return Problem.builder()
            .number(number)
            .title(title)
            .link(url)
            .build();
    }
}