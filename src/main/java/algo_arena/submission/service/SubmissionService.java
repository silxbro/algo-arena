package algo_arena.submission.service;

import static algo_arena.submission.enums.SubmissionResult.CORRECT;

import algo_arena.member.entity.Member;
import algo_arena.member.service.MemberService;
import algo_arena.submission.dto.request.SubmissionSearchRequest;
import algo_arena.submission.entity.PendingSubmission;
import algo_arena.submission.entity.Submission;
import algo_arena.submission.repository.PendingSubmissionRedisRepository;
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
    private final PendingSubmissionRedisRepository pendingSubmissionRepository;
    private final MemberService memberService;

    @Transactional
    public Long submitSolution(String roomId, PendingSubmission pendingSubmission) {
        Long problemNumber = pendingSubmission.getProblemNumber();
        String memberName = pendingSubmission.getMemberName();
        if (pendingSubmissionRepository.hasSubmitted(roomId, problemNumber, memberName)) {
            throw new RuntimeException();
        }
        return pendingSubmissionRepository.save(roomId, pendingSubmission);
    }

    public List<PendingSubmission> findPendingSubmissions(String roomId, Long problemNumber) {
        return pendingSubmissionRepository.findAllByRoomProblem(roomId, problemNumber);
    }

    public PendingSubmission findPendingSubmission(String roomId, Long problemNumber, String memberName, String requestMemberName) {
        if (!pendingSubmissionRepository.hasSubmittedCorrectly(roomId, problemNumber, requestMemberName)) {
            throw new RuntimeException();
        }
        return pendingSubmissionRepository.findOne(roomId, problemNumber, memberName).orElseThrow();
    }

    public List<Submission> findSubmissionsBySearch(SubmissionSearchRequest request) {
        return submissionRepository.findSubmissions(request);
    }

    public Submission findSubmission(Long id, String memberName) {
        Submission submission = submissionRepository.findById(id).orElseThrow();

        Member member = memberService.findMemberByName(memberName);
        if (!submissionRepository.existSubmission(submission.getProblem(), member, CORRECT)) {
            throw new RuntimeException();
        }

        return submission;
    }
}