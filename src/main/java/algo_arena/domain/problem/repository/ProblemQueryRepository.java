package algo_arena.domain.problem.repository;

import algo_arena.domain.problem.entity.Problem;
import java.util.List;

public interface ProblemQueryRepository {

    List<Problem> findProblemsBySearch(Long minNumber, Long maxNumber, String title);

}