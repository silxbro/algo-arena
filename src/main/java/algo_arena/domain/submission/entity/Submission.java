package algo_arena.domain.submission.entity;

import algo_arena.common.entity.BaseEntity;
import algo_arena.domain.member.entity.Member;
import algo_arena.domain.problem.entity.Problem;
import algo_arena.domain.submission.enums.CodeLanguage;
import algo_arena.domain.submission.enums.SubmissionResult;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Submission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @EqualsAndHashCode.Include
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    @EqualsAndHashCode.Include
    private Problem problem;

    @EqualsAndHashCode.Include
    private Long index;

    @Enumerated(EnumType.STRING)
    private CodeLanguage language;

    @Enumerated(EnumType.STRING)
    private SubmissionResult result;

    @Lob
    private String solutionCode;
}