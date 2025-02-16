package algo_arena.submission.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PendingSubmissionFindRequest {

    private Long problemNumber;
    private String memberName;

}