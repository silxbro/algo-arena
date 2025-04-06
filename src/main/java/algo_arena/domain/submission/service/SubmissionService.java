package algo_arena.domain.submission.service;

import static algo_arena.common.exception.enums.ErrorType.*;

import algo_arena.domain.member.entity.Member;
import algo_arena.domain.member.service.MemberService;
import algo_arena.domain.problem.entity.Problem;
import algo_arena.domain.problem.service.ProblemService;
import algo_arena.domain.room.service.RoomFindService;
import algo_arena.domain.submission.dto.request.SubmissionSearchRequest;
import algo_arena.domain.submission.entity.Submission;
import algo_arena.domain.submission.enums.SubmissionResult;
import algo_arena.domain.submission.exception.SubmissionException;
import algo_arena.domain.submission.repository.PendingSubmissionRedisRepository;
import algo_arena.domain.submission.entity.PendingSubmission;
import algo_arena.domain.submission.repository.SubmissionRepository;
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
    private final RoomFindService roomFindService;
    private final MemberService memberService;
    private final ProblemService problemService;

    @Transactional
    public Long submitSolution(String roomId, PendingSubmission pendingSubmission) {
        Long problemNumber = pendingSubmission.getProblemNumber();
        String memberName = pendingSubmission.getMemberName();

        validatePendingSubmission(roomId, problemNumber, memberName);

        if (pendingSubmissionRepository.hasSubmitted(roomId, problemNumber, memberName)) {
            throw new SubmissionException(SUBMISSION_DUPLICATED);
        }
        return pendingSubmissionRepository.save(roomId, pendingSubmission);
    }

    @Transactional
    public Long approveSubmission(String roomId, Long problemNumber, String memberName, String requestMemberName) {
        PendingSubmission pendingSubmission = findPendingSubmission(roomId, problemNumber, memberName, requestMemberName);

        Member member = memberService.findMemberByName(memberName);
        Problem problem = problemService.findProblemByNumber(problemNumber);
        Long index = submissionRepository.findLastIndex(member, problem).orElse(0L) + 1;

        if (!pendingSubmissionRepository.hasSubmittedCorrectly(roomId, problemNumber, memberName)) { //승인 자격 검사
            throw new SubmissionException(SUBMISSION_APPROVAL_NOT_ALLOWED);
        }
        Submission submission = pendingSubmission.confirm(member, problem, index);
        submissionRepository.save(submission);

        return pendingSubmissionRepository.set(roomId, pendingSubmission);
    }

    public List<PendingSubmission> findPendingSubmissions(String roomId, Long problemNumber, String requestMemberName) {
        validatePendingSubmission(roomId, problemNumber, requestMemberName);
        return pendingSubmissionRepository.findAllByRoomProblem(roomId, problemNumber);
    }

    public PendingSubmission findPendingSubmission(String roomId, Long problemNumber, String memberName, String requestMemberName) {
        validatePendingSubmission(roomId, problemNumber, requestMemberName);
        if (!pendingSubmissionRepository.hasSubmittedCorrectly(roomId, problemNumber, requestMemberName)) {
            throw new SubmissionException(SUBMISSION_REVIEW_NOT_ALLOWED);
        }
        return pendingSubmissionRepository.findOne(roomId, problemNumber, memberName)
            .orElseThrow(() -> new SubmissionException(SUBMISSION_NOT_FOUND));
    }

    public List<Submission> findSubmissionsBySearch(SubmissionSearchRequest request) {
        return submissionRepository.findSubmissions(request);
    }

    public Submission findSubmission(Long id, String memberName) {
        if (id == null) {
            throw new SubmissionException(NULL_VALUE);
        }

        Submission submission = submissionRepository.findById(id)
            .orElseThrow(() -> new SubmissionException(SUBMISSION_NOT_FOUND));

        Member member = memberService.findMemberByName(memberName);
        if (!submissionRepository.existSubmission(submission.getProblem(), member, SubmissionResult.CORRECT)) {
            throw new SubmissionException(SUBMISSION_REVIEW_NOT_ALLOWED);
        }

        return submission;
    }

    private void validatePendingSubmission(String roomId, Long problemNumber, String memberName) {
        memberService.findMemberByName(memberName);
        roomFindService.findRoomById(roomId, memberName);
        problemService.findProblemByNumber(problemNumber);
    }
}