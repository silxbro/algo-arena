package algo_arena.submission.repository;

import algo_arena.member.entity.Member;
import algo_arena.problem.entity.Problem;
import algo_arena.submission.entity.Submission;
import algo_arena.submission.enums.SubmissionResult;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long>, SubmissionQueryRepository {

    @Query("select count(s) > 0 from Submission s where s.problem = :problem and s.member = :member and s.result = :result")
    boolean existSubmission(@Param("problem") Problem problem, @Param("member") Member member, @Param("result") SubmissionResult result);

}