package algo_arena.submission.repository;

import algo_arena.submission.dto.request.SubmissionSearchRequest;
import algo_arena.submission.entity.Submission;
import java.util.List;

public interface SubmissionQueryRepository {
    List<Submission> findSubmissions(SubmissionSearchRequest searchCond);
}
