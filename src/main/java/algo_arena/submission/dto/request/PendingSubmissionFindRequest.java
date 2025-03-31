package algo_arena.submission.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PendingSubmissionFindRequest {

    @NotNull
    @Positive
    private Long problemNumber;

    @NotBlank
    private String memberName;

}