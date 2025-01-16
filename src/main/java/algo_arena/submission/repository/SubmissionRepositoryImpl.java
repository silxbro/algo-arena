package algo_arena.submission.repository;

import static algo_arena.member.entity.QMember.member;
import static algo_arena.problem.entity.QProblem.problem;
import static algo_arena.submission.entity.QSubmission.submission;

import algo_arena.submission.dto.request.SubmissionSearchRequest;
import algo_arena.submission.entity.JudgementResult;
import algo_arena.submission.entity.Language;
import algo_arena.submission.entity.Submission;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.util.StringUtils;

public class SubmissionRepositoryImpl implements SubmissionQueryRepository {

    private final JPAQueryFactory query;

    public SubmissionRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<Submission> findSubmissions(SubmissionSearchRequest request) {
        Long problemNumber = request.getProblemNumber();
        String memberName = request.getMemberName();
        String languageName = request.getLanguageName();
        String resultDescription = request.getResultDescription();

        return query
            .select(submission)
            .from(submission)
            .join(submission.member, member)
            .join(submission.problem, problem)
            .where(problemEq(problemNumber), memberEq(memberName), languageEq(languageName), resultEq(resultDescription))
            .fetch();
    }

    private BooleanExpression problemEq(Long problemNumber) {
        if (problemNumber == null) {
            return null;
        }
        return problem.number.eq(problemNumber);
    }

    private BooleanExpression memberEq(String memberName) {
        if (!StringUtils.hasText(memberName)) {
            return null;
        }
        return member.name.eq(memberName);
    }

    private BooleanExpression languageEq(String languageName) {
        Language language = Language.fromName(languageName);
        if (language == null) {
            return null;
        }
        return submission.language.eq(language);
    }

    private BooleanExpression resultEq(String resultDescription) {
        JudgementResult result = JudgementResult.fromDescription(resultDescription);
        if (result == null) {
            return null;
        }
        return submission.result.eq(result);
    }
}