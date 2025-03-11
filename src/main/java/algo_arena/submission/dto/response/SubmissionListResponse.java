package algo_arena.submission.dto.response;

import algo_arena.submission.entity.Submission;
import java.util.Collection;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubmissionListResponse {

    private long number;
    private List<SubmissionBasicResponse> submissionList;

    public static SubmissionListResponse from(Collection<Submission> submissions) {

        return SubmissionListResponse.builder()
            .number(submissions.size())
            .submissionList(submissions.stream().map(SubmissionBasicResponse::from).toList())
            .build();
    }
}