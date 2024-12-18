package algo_arena.member.entity;

import algo_arena.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(updatable = false)
    private String email;

    private String password;

    @Column(updatable = false)
    private String nickname;
    private String imgUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    private int testCount; //참가 테스트 개수
    private int problemCount; //정답 문제 개수

}