package algo_arena.problem.controller;

import algo_arena.problem.dto.request.ProblemRegisterRequest;
import algo_arena.problem.dto.request.ProblemSearchRequest;
import algo_arena.problem.dto.request.ProblemUpdateRequest;
import algo_arena.problem.dto.response.ProblemListResponse;
import algo_arena.problem.dto.response.ProblemRegisterResponse;
import algo_arena.problem.entity.Problem;
import algo_arena.problem.service.ProblemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
public class ProblemApiController {

    private final ProblemService problemService;

    /**
     * 테스트문제 등록
     */
    @PostMapping
    public ResponseEntity<ProblemRegisterResponse> registerProblem(@AuthenticationPrincipal UserDetails userDetails,
        @RequestBody ProblemRegisterRequest request) {

        String username = userDetails.getUsername();
        Problem newProblem = problemService.registerProblem(request.toEntity(), username);

        return ResponseEntity.ok(ProblemRegisterResponse.from(newProblem));
    }

    /**
     * 테스트문제 검색(조회)
     */
    @GetMapping
    public ResponseEntity<ProblemListResponse> searchProblems(@RequestBody ProblemSearchRequest request) {

        List<Problem> problems = problemService.findProblemsBySearch(
            request.getMinNumber(), request.getMaxNumber(), request.getTitle());

        return ResponseEntity.ok(ProblemListResponse.from(problems));
    }

    /**
     * 테스트문제 수정
     */
    @PostMapping("/{problemNumber}")
    public ResponseEntity<Void> updateProblem(@AuthenticationPrincipal UserDetails userDetails,
        @PathVariable("problemNumber") Long problemNumber, @RequestBody ProblemUpdateRequest request) {

        String username = userDetails.getUsername();
        problemService.updateProblem(problemNumber, request.getTitle(), request.getLink(), username);

        return ResponseEntity.ok().build();
    }

    /**
     * 테스트문제 삭제
     */
    @DeleteMapping("/{problemNumber}")
    public ResponseEntity<Void> deleteProblem(@AuthenticationPrincipal UserDetails userDetails,
        @PathVariable("problemNumber") Long problemNumber) {

        String username = userDetails.getUsername();
        problemService.deleteProblem(problemNumber, username);

        return ResponseEntity.ok().build();
    }
}