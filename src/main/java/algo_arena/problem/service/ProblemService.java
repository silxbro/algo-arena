package algo_arena.problem.service;

import static algo_arena.common.exception.enums.ErrorType.*;

import algo_arena.member.entity.Member;
import algo_arena.member.exception.AuthException;
import algo_arena.member.service.MemberService;
import algo_arena.problem.entity.Problem;
import algo_arena.problem.exception.ProblemException;
import algo_arena.problem.repository.ProblemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final MemberService memberService;

    @Transactional
    public Problem registerProblem(Problem problem, String memberName) {
        checkAdminAuth(memberName);
        return problemRepository.save(problem);
    }

    public List<Problem> findProblemsBySearch(Long minNumber, Long maxNumber, String title) {
        if (minNumber != null && maxNumber != null && minNumber > maxNumber) {
            throw new ProblemException(INVALID_PROBLEM_NUMBER_RANGE);
        }
        return problemRepository.findProblemsBySearch(minNumber, maxNumber, title);
    }

    public Problem findProblemByNumber(Long number) {
        if (number == null) {
            throw new ProblemException(NULL_VALUE);
        }
        return problemRepository.findByNumber(number)
            .orElseThrow(() -> new ProblemException(PROBLEM_NOT_FOUND));
    }

    @Transactional
    public void updateProblem(Long number, String title, String link, String memberName) {
        checkAdminAuth(memberName);
        Problem problem = findProblemByNumber(number);

        if (StringUtils.hasText(title)) {
            problem.changeTitle(title);
        }
        if (StringUtils.hasText(link)) {
            problem.changeLink(link);
        }
    }

    @Transactional
    public void deleteProblem(Long number, String memberName) {
        checkAdminAuth(memberName);
        if (number == null) {
            throw new ProblemException(NULL_VALUE);
        }
        problemRepository.deleteByNumber(number);
    }

    private void checkAdminAuth(String memberName) {
        Member member = memberService.findMemberByName(memberName);
        if (!member.isAdmin()) {
            throw new AuthException(INVALID_ROLE);
        }
    }
}