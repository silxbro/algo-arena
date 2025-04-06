package algo_arena.domain.submission.controller;

import algo_arena.domain.submission.dto.request.SubmissionSearchRequest;
import algo_arena.domain.submission.dto.response.SubmissionDetailResponse;
import algo_arena.domain.submission.dto.response.SubmissionListResponse;
import algo_arena.domain.submission.entity.Submission;
import algo_arena.domain.submission.service.SubmissionService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionApiController {

    private final SubmissionService submissionService;

    /**
     * 문제풀이 제출 목록 (목록 검색)
     */
    @GetMapping
    public ResponseEntity<SubmissionListResponse> findSubmissionsBySearch(@Valid @RequestBody SubmissionSearchRequest request) {

        List<Submission> submissions = submissionService.findSubmissionsBySearch(request);

        return ResponseEntity.ok(SubmissionListResponse.from(submissions));
    }

    /**
     * 문제풀이 제출 코드 (상세 검색)
     */
    @GetMapping("/{submissionId}")
    public ResponseEntity<SubmissionDetailResponse> findSubmission(@PathVariable(value = "submissionId", required = false) Long submissionId,
        @AuthenticationPrincipal UserDetails userDetails) {

        String memberName = userDetails.getUsername();

        Submission submission = submissionService.findSubmission(submissionId, memberName);

        return ResponseEntity.ok(SubmissionDetailResponse.from(submission));
    }

}