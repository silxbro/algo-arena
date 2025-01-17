package algo_arena.problem.service;

import algo_arena.member.entity.Member;
import algo_arena.member.repository.MemberRepository;
import algo_arena.problem.entity.Problem;
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
    private final MemberRepository memberRepository;

    @Transactional
    public Problem registerProblem(Problem problem, String memberName) {
        checkAuth(memberName);
        return problemRepository.save(problem);
    }

    public List<Problem> findProblemsBySearch(Long minNumber, Long maxNumber, String title) {
        return problemRepository.findProblemsBySearch(minNumber, maxNumber, title);
    }

    public Problem findProblemByNumber(Long id) {
        return problemRepository.findByNumber(id).orElseThrow();
    }

    @Transactional
    public void updateProblem(Long number, String title, String link, String memberName) {
        checkAuth(memberName);

        Problem problem = findProblemByNumber(number);
        if (StringUtils.hasText(title)) {
            problem.changeTitle(title);
        }
        if (StringUtils.hasText(link)) {
            problem.changeLink(title);
        }
    }

    @Transactional
    public void deleteProblem(Long number, String memberName) {
        checkAuth(memberName);
        problemRepository.deleteByNumber(number);
    }

    private void checkAuth(String memberName) {
        Member member = memberRepository.findByName(memberName).orElseThrow();
        if (!member.isAdmin()) {
            throw new RuntimeException("권한이 없습니다. 관리자에게 문의하세요.");
        }
    }
}