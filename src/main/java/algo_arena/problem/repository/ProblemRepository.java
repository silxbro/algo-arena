package algo_arena.problem.repository;

import algo_arena.problem.entity.Problem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long>, ProblemQueryRepository {

    Optional<Problem> findByNumber(Long number);

    void deleteByNumber(Long number);

}