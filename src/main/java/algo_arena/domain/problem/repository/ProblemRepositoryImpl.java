package algo_arena.domain.problem.repository;


import static algo_arena.domain.problem.entity.QProblem.problem;

import algo_arena.domain.problem.entity.Problem;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.util.StringUtils;

public class ProblemRepositoryImpl implements ProblemQueryRepository {

    private final JPAQueryFactory query;

    public ProblemRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<Problem> findProblemsBySearch(Long minNumber, Long maxNumber, String title) {
        return query
            .selectFrom(problem)
            .where(
                numberBetween(minNumber, maxNumber),
                titleContains(title)
            )
            .fetch();
    }

    private BooleanExpression numberBetween(Long minNumber, Long maxNumber) {
        if (minNumber == null && maxNumber == null) {
            return null;
        }
        if (maxNumber == null) {
            return problem.number.goe(minNumber);
        }
        if (minNumber == null) {
            return problem.number.loe(maxNumber);
        }
        return problem.number.between(minNumber, maxNumber);
    }

    private BooleanExpression titleContains(String title) {
        if (!StringUtils.hasText(title)) {
            return null;
        }
        return problem.title.toUpperCase().contains(title.toUpperCase());
    }
}