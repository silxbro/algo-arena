package algo_arena.chat.entity;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
class ChatLogTest {

    ChatLog chatLog;

    @BeforeEach
    void setUp() {
        chatLog = ChatLog.builder()
            .type(ClientMessageType.CHAT)
            .message("hello!")
            .senderId(1L)
            .build();
    }

    @Test
    @DisplayName("필드값 확인")
    void values() {
        //default
        log.info("chatLog.id={}", chatLog.getId());
        log.info("chatLog.index={}", chatLog.getIndex());
        log.info("chatLog.sendTime={}", chatLog.getSendTime());
        //custom
        log.info("chatLog.type={}", chatLog.getType());
        log.info("chatLog.message={}", chatLog.getMessage());
        log.info("chatLog.senderId={}", chatLog.getSenderId());
    }

    @Test
    @DisplayName("채팅 메시지 내용을 변경할 수 있다")
    void updateMessage() {
        //given

        //when
        chatLog.updateMessage("안녕하세요!");

        //then
        assertThat(chatLog.getMessage()).isEqualTo("안녕하세요!");
    }

    @Test
    @DisplayName("채팅 인덱스를 초기화할 수 있다")
    void initIndex() {
        //given

        //when
        chatLog.initIndex(10L);

        //then
        assertThat(chatLog.getIndex()).isEqualTo(10L);
    }
}