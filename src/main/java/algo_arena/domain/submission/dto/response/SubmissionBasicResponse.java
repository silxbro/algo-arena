package algo_arena.domain.submission.dto.response;

import algo_arena.domain.submission.entity.Submission;
import java.sql.Timestamp;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubmissionBasicResponse {

    private Long submissionId;
    private String memberName;
    private Long problemNumber;
    private Long index;
    private String resultDescription;
    private String languageName;
    private Timestamp submittedAt;

    public static SubmissionBasicResponse from(Submission submission) {
        return SubmissionBasicResponse.builder()
            .submissionId(submission.getId())
            .memberName(submission.getMember().getName())
            .problemNumber(submission.getProblem().getNumber())
            .index(submission.getIndex())
            .resultDescription(submission.getResult().getDescription())
            .languageName(submission.getLanguage().getName())
            .submittedAt(submission.getCreatedAt())
            .build();
    }
}