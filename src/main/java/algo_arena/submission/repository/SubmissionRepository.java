package algo_arena.submission.repository;

import algo_arena.submission.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long>, SubmissionQueryRepository {

}