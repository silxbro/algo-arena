package algo_arena.submission.repository;

import algo_arena.submission.dto.request.SubmissionSearchCond;
import algo_arena.submission.entity.Submission;
import java.util.List;

public interface SubmissionQueryRepository {
    List<Submission> findSubmissions(SubmissionSearchCond searchCond);
}
