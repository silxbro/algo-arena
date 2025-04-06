package algo_arena.domain.submission.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PendingSubmissionApproveResponse {

    private String roomId;
    private Long problemNumber;
    private String memberName;
    private Long index;

    public static PendingSubmissionApproveResponse from(String roomId, Long problemNumber, String memberName, Long index) {
        return PendingSubmissionApproveResponse.builder()
            .roomId(roomId)
            .problemNumber(problemNumber)
            .memberName(memberName)
            .index(index)
            .build();
    }
}