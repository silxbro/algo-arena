package algo_arena.domain.submission.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PendingSubmissionApproveRequest {

    @NotNull
    @Positive
    private Long problemNumber;

    @NotBlank
    private String memberName;
}