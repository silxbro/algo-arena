package algo_arena.submission.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PendingSubmissionCreateResponse {

    private String roomId;
    private Long problemNumber;
    private String memberName;
    private Long index;

    public static PendingSubmissionCreateResponse from(String roomId, Long problemNumber, String memberName, Long index) {
        return PendingSubmissionCreateResponse.builder()
            .roomId(roomId)
            .problemNumber(problemNumber)
            .memberName(memberName)
            .index(index)
            .build();
    }
}