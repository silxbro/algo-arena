package algo_arena.submission.service;

import algo_arena.submission.dto.request.SubmissionSearchCond;
import algo_arena.submission.entity.Submission;
import algo_arena.submission.repository.SubmissionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;

    @Transactional
    public Submission create(Submission submission) {
        return submissionRepository.save(submission);
    }

    public Submission findOneById(Long id) {
        return submissionRepository.findById(id).orElseThrow();
    }

    public List<Submission> findAll(SubmissionSearchCond searchCond) {
        return submissionRepository.findSubmissions(searchCond);
    }
}