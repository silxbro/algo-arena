package algo_arena.submission.service;

import algo_arena.submission.entity.PendingSubmission;
import algo_arena.submission.repository.PendingSubmissionRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubmissionService {

    private final PendingSubmissionRedisRepository pendingSubmissionRepository;

    @Transactional
    public Long submitSolution(String roomId, PendingSubmission pendingSubmission) {
        Long problemNumber = pendingSubmission.getProblemNumber();
        String memberName = pendingSubmission.getMemberName();
        if (pendingSubmissionRepository.hasSubmitted(roomId, problemNumber, memberName)) {
            throw new RuntimeException();
        }
        return pendingSubmissionRepository.save(roomId, pendingSubmission);
    }
}