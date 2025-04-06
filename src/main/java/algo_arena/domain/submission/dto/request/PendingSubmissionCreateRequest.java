package algo_arena.domain.submission.dto.request;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PendingSubmissionCreateRequest {

    @NotNull
    @Positive
    private Long problemNumber;

    @NotBlank
    private String languageName;

    @NotBlank
    private String resultDescription;

    @NotBlank
    private String resultLink;

    @NotBlank
    @Lob
    private String solutionCode;
}