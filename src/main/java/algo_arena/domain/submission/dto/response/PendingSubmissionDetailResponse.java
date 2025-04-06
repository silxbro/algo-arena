package algo_arena.domain.submission.dto.response;

import algo_arena.domain.submission.entity.PendingSubmission;
import jakarta.persistence.Lob;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PendingSubmissionDetailResponse {

    private String roomId;
    private Long problemNumber;
    private String memberName;
    private String languageName;
    private String resultDescription;
    private String resultLink;
    private Boolean isConfirmed;

    @Lob
    private String solutionCode;

    public static PendingSubmissionDetailResponse from(String roomId, PendingSubmission pendingSubmission) {
        return PendingSubmissionDetailResponse.builder()
            .roomId(roomId)
            .problemNumber(pendingSubmission.getProblemNumber())
            .memberName(pendingSubmission.getMemberName())
            .languageName(pendingSubmission.getLanguageName())
            .resultDescription(pendingSubmission.getResultDescription())
            .resultLink(pendingSubmission.getResultLink())
            .solutionCode(pendingSubmission.getSolutionCode())
            .isConfirmed(pendingSubmission.getIsApproved())
            .build();
    }
}