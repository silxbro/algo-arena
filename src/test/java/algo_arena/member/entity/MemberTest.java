package algo_arena.member.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
            .id(1L)
            .email("test@example.com")
            .password("password")
            .nickname("tester")
            .build();
    }

    @Test
    @DisplayName("회원 entity id 동등여부를 조회할 수 있다")
    void equalsId() {
        //given

        //when
        boolean equalResult = member.equalsId(member.getId());
        boolean notEqualResult = member.equalsId(member.getId() + 1);

        //then
        assertThat(equalResult).isTrue();
        assertThat(notEqualResult).isFalse();
    }

    @Test
    @DisplayName("회원 entity 비밀번호를 변경할 수 있다")
    void changePassword() {
        //given
        String newPassword = "newPassword";

        //when
        member.changePassword(newPassword);

        //then
        assertThat(member.getPassword()).isEqualTo(newPassword);
    }
}