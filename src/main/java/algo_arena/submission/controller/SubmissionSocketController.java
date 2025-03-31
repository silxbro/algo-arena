package algo_arena.submission.controller;

import algo_arena.submission.dto.request.PendingSubmissionApproveRequest;
import algo_arena.submission.dto.request.PendingSubmissionFindRequest;
import algo_arena.submission.dto.request.PendingSubmissionCreateRequest;
import algo_arena.submission.dto.response.PendingSubmissionApproveResponse;
import algo_arena.submission.dto.response.PendingSubmissionCreateResponse;
import algo_arena.submission.dto.response.PendingSubmissionDetailResponse;
import algo_arena.submission.entity.PendingSubmission;
import algo_arena.submission.service.SubmissionService;
import algo_arena.utils.jwt.service.JwtTokenUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SubmissionSocketController {

    private final SubmissionService submissionService;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * 문제풀이 코드 제출
     */
    @MessageMapping("/rooms/{roomId}/submit")
    @SendTo("/sub/rooms/{roomId}/submit")
    public PendingSubmissionCreateResponse submitSolution(@DestinationVariable("roomId") String roomId, @Header("token") String token,
        @Valid @RequestBody PendingSubmissionCreateRequest request) {

        String memberName = jwtTokenUtil.extractUsername(token);
        Long problemNumber = request.getProblemNumber();
        PendingSubmission pendingSubmission = PendingSubmission.create(problemNumber, memberName,
            request.getLanguageName(), request.getResultDescription(), request.getResultLink(), request.getSolutionCode());

        Long index = submissionService.submitSolution(roomId, pendingSubmission);
        return PendingSubmissionCreateResponse.from(roomId, problemNumber, memberName, index);
    }

    /**
     * 문제풀이 코드 상세 조회
     */
    @MessageMapping("/rooms/{roomId}/submission")
    @SendTo("/sub/rooms/{roomId}/submission")
    public PendingSubmissionDetailResponse findPendingSubmission(@DestinationVariable("roomId") String roomId, @Header("token") String token,
        @Valid @RequestBody PendingSubmissionFindRequest request) {

        Long problemNumber = request.getProblemNumber();
        String memberName = request.getMemberName();
        String requestMemberName = jwtTokenUtil.extractUsername(token);

        PendingSubmission pendingSubmission = submissionService.findPendingSubmission(roomId, problemNumber, memberName, requestMemberName);

        return PendingSubmissionDetailResponse.from(roomId, pendingSubmission);
    }

    /**
     * 문제풀이 코드 승인
     */
    @MessageMapping("/rooms/{roomId}/submission/approve")
    @SendTo("/sub/rooms/{roomId}/approve")
    public PendingSubmissionApproveResponse approveSubmission(@DestinationVariable("roomId") String roomId, @Header("token") String token,
        @Valid @RequestBody PendingSubmissionApproveRequest request) {

        Long problemNumber = request.getProblemNumber();
        String memberName = request.getMemberName();
        String requestMemberName = jwtTokenUtil.extractUsername(token);

        Long index = submissionService.approveSubmission(roomId, problemNumber, memberName, requestMemberName);

        return PendingSubmissionApproveResponse.from(roomId, problemNumber, memberName, index);
    }
}