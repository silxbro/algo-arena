package algo_arena.utils.auth.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CodeGeneratorTest {

    CodeGenerator codeGenerator;

    @BeforeEach
    void setUp() {
        codeGenerator = new CodeGenerator();
    }

    @Test
    @DisplayName("영문 대소문자, 숫자로 이루어진 인증코드를 요청 길이만큼 생성한다")
    void generateAuthCode() {
        //given
        int length = 10;

        //when
        String code = codeGenerator.generateAuthCode(10);

        //then
        assertThat(code).isNotNull();
        assertThat(code.length()).isEqualTo(length);
        assertThat(code).matches("^[A-Za-z0-9]+$");
    }
}