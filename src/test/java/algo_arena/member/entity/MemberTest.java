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
            .name("tester")
            .build();
    }

    @Test
    @DisplayName("회원 entity 의 이름 여부를 조회할 수 있다")
    void isName() {
        //given

        //when
        boolean equalResult = member.isName(member.getName());
        boolean notEqualResult = member.isName("name");

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