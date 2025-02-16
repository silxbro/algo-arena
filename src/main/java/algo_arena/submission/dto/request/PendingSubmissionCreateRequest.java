package algo_arena.submission.dto.request;

import jakarta.persistence.Lob;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PendingSubmissionCreateRequest {

    private Long problemNumber;
    private String languageName;
    private String resultDescription;
    private String resultLink;

    @Lob
    private String solutionCode;
}