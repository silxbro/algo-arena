package algo_arena.domain.submission.repository;

import algo_arena.domain.submission.dto.request.SubmissionSearchRequest;
import algo_arena.domain.submission.entity.Submission;
import java.util.List;

public interface SubmissionQueryRepository {
    List<Submission> findSubmissions(SubmissionSearchRequest searchCond);
}
