package algo_arena.problem.repository;

import algo_arena.problem.entity.Problem;
import java.util.List;

public interface ProblemQueryRepository {

    List<Problem> findProblemsBySearch(Long minNumber, Long maxNumber, String title);

}