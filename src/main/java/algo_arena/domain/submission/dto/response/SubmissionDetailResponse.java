package algo_arena.domain.submission.dto.response;

import algo_arena.domain.submission.entity.Submission;
import jakarta.persistence.Lob;
import java.sql.Timestamp;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubmissionDetailResponse {

    private Long submissionId;
    private String memberName;
    private Long problemNumber;
    private String problemTitle;
    private String resultDescription;
    private String languageName;
    private Timestamp submittedAt;

    @Lob
    private String solutionCode;

    public static SubmissionDetailResponse from(Submission submission) {
        return SubmissionDetailResponse.builder()
            .submissionId(submission.getId())
            .memberName(submission.getMember().getName())
            .problemNumber(submission.getProblem().getNumber())
            .problemTitle(submission.getProblem().getTitle())
            .resultDescription(submission.getResult().getDescription())
            .languageName(submission.getLanguage().getName())
            .submittedAt(submission.getCreatedAt())
            .solutionCode(submission.getSolutionCode())
            .build();
    }
}