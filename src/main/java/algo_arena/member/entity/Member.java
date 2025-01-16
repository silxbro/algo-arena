package algo_arena.member.entity;

import algo_arena.common.entity.BaseEntity;
import algo_arena.member.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@EqualsAndHashCode(of = {"id", "email", "name"}, callSuper = false)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(updatable = false)
    private String email;

    @JsonIgnore
    private String password;

    @Column(updatable = false)
    private String name;

    @Column(updatable = false)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    private long testCount; //참가 테스트 개수
    private long submissionCount; // 제출 문제 개수
    private long rightCount; //정답 문제 개수
    private long wrongCount; //오답 문제 개수
    private long timeExceedCount; //시간 초과 문제 개수
    private long memoryExceedCount; //메모리 초과 문제 개수
    private long compileErrorCount; //컴파일 오류 문제 개수
    private long runtimeErrorCount; //런타임 오류 문제 개수
    private long outputExceedCount; //출력 초과 문제 개수
    private long formatErrorCount; //출력 형식 오류 개수

    public boolean isName(String memberName) {
        return name.equals(memberName);
    }

    public void changePassword(String password) {
        this.password = password;
    }
}