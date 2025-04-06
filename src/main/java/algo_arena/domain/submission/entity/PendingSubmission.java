package algo_arena.domain.submission.entity;

import algo_arena.common.entity.BaseEntity;
import algo_arena.domain.member.entity.Member;
import algo_arena.domain.problem.entity.Problem;
import algo_arena.domain.submission.enums.SubmissionResult;
import algo_arena.domain.submission.enums.CodeLanguage;
import jakarta.persistence.Lob;
import java.io.Serializable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@RedisHash("submission")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class PendingSubmission extends BaseEntity implements Serializable {

    @Id
    @Builder.Default
    @EqualsAndHashCode.Include
    private String id = UUID.randomUUID().toString();

    private Long problemNumber;
    private String memberName;
    private String languageName;
    private String resultDescription;
    private String resultLink;

    @Lob
    private String solutionCode;

    @Builder.Default
    private Boolean isApproved = false;

    public static PendingSubmission create(
        Long problemNumber, String memberName, String languageName, String resultDescription, String resultLink, String solutionCode) {

        return PendingSubmission.builder()
            .problemNumber(problemNumber)
            .memberName(memberName)
            .languageName(languageName)
            .resultDescription(resultDescription)
            .resultLink(resultLink)
            .solutionCode(solutionCode)
            .build();
    }

    public boolean isCorrectResult() {
        return resultDescription.equals(SubmissionResult.CORRECT.getDescription()) && isApproved;
    }

    public Submission confirm(Member member, Problem problem, Long index) {
        isApproved = true;
        return Submission.builder()
            .member(member)
            .problem(problem)
            .index(index)
            .language(CodeLanguage.fromName(languageName))
            .result(SubmissionResult.fromDescription(resultDescription))
            .solutionCode(solutionCode)
            .build();
    }
}