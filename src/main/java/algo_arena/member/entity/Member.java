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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    @Builder.Default
    private String imgUrl = "EMPTY";

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;

    private long testCount; //참가 테스트 개수
    private long problemCount; //정답 문제 개수

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeImage(String imageUrl) {
        this.imgUrl = imageUrl;
    }
}