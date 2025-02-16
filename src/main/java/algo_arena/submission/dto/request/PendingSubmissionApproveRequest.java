package algo_arena.submission.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PendingSubmissionApproveRequest {
    private Long problemNumber;
    private String memberName;
}