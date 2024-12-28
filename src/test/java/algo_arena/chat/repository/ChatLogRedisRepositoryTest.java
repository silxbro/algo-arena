package algo_arena.chat.repository;

import static org.assertj.core.api.Assertions.assertThat;

import algo_arena.chat.entity.ChatLog;
import algo_arena.config.redis.RedisConfig;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(RedisConfig.class)
class ChatLogRedisRepositoryTest {

    static final String TEST_ROOM_ID = "test-room";

    @Autowired
    ChatLogRedisRepository chatLogRepository;

    @AfterEach
    void clear() {
        chatLogRepository.deleteByRoomId(TEST_ROOM_ID);
    }

    @Test
    @DisplayName("채팅이 채팅방별로 알맞게 저장되며, 해당 채팅방의 채팅목록을 조회할 수 있다")
    void saveAndFindAllByRoomId() {
        //given
        ChatLog chatLog1 = ChatLog.builder().message("hello!").senderNickname("member1").build();
        ChatLog chatLog2 = ChatLog.builder().message("안녕하세요!").senderNickname("member2").build();
        ChatLog chatLog3 = ChatLog.builder().message("반갑습니다^^").senderNickname("member3").build();

        //when
        chatLogRepository.saveByRoomId(TEST_ROOM_ID, chatLog1);
        chatLogRepository.saveByRoomId(TEST_ROOM_ID, chatLog2);
        chatLogRepository.saveByRoomId(TEST_ROOM_ID, chatLog3);

        List<ChatLog> chatLogs = chatLogRepository.findAllByRoomId(TEST_ROOM_ID);
        List<ChatLog> emptyLogs = chatLogRepository.findAllByRoomId("room");

        //then
        assertThat(chatLogs).containsExactly(chatLog1, chatLog2, chatLog3);
        assertThat(emptyLogs).isEmpty();
    }

    @Test
    @DisplayName("채팅방 ID 및 index 로 개별 채팅을 조회할 수 있다")
    void findOneByRoomIdAndIndex() {
        //given
        ChatLog chatLog1 = ChatLog.builder().message("hello!").senderNickname("member1").build();
        ChatLog chatLog2 = ChatLog.builder().message("안녕하세요!").senderNickname("member2").build();

        Long index1 = chatLogRepository.saveByRoomId(TEST_ROOM_ID, chatLog1);
        Long index2 = chatLogRepository.saveByRoomId(TEST_ROOM_ID, chatLog2);

        //when
        ChatLog findChatLog1 = chatLogRepository.findOneByRoomIdAndIndex(TEST_ROOM_ID, index1).orElse(null);
        ChatLog findChatLog2 = chatLogRepository.findOneByRoomIdAndIndex(TEST_ROOM_ID, index2).orElse(null);

        //then
        assertThat(findChatLog1).isEqualTo(chatLog1);
        assertThat(findChatLog2).isEqualTo(chatLog2);
    }

    @Test
    @DisplayName("채팅방 ID 및 index 로 채팅을 수정할 수 있다")
    void updateByRoomIdAndIndex() {
        //given
        ChatLog chatLog = ChatLog.builder().message("hello!").senderNickname("member").build();
        Long index = chatLogRepository.saveByRoomId(TEST_ROOM_ID, chatLog);

        //when
        ChatLog newChatLog = ChatLog.builder().message("안녕하세요!!!").senderNickname("new-member").build();
        chatLogRepository.updateByRoomIdAndIndex(TEST_ROOM_ID, index, newChatLog);
        ChatLog updatedChatLog = chatLogRepository.findOneByRoomIdAndIndex(TEST_ROOM_ID, index).orElse(null);

        //then
        assertThat(updatedChatLog).isNotNull();
        assertThat(updatedChatLog.getMessage()).isEqualTo("안녕하세요!!!");
        assertThat(updatedChatLog.getSenderNickname()).isEqualTo("new-member");
    }

    @Test
    @DisplayName("채팅방 ID 로 채팅을 삭제할 수 있다")
    void deleteByRoomId() {
        //given
        ChatLog chatLog1 = ChatLog.builder().message("hello!").senderNickname("member1").build();
        ChatLog chatLog2 = ChatLog.builder().message("안녕하세요!").senderNickname("member2").build();

        chatLogRepository.saveByRoomId(TEST_ROOM_ID, chatLog1);
        chatLogRepository.saveByRoomId(TEST_ROOM_ID, chatLog2);

        //when
        chatLogRepository.deleteByRoomId(TEST_ROOM_ID);
        List<ChatLog> chatLogs = chatLogRepository.findAllByRoomId(TEST_ROOM_ID);

        //then
        assertThat(chatLogs).isEmpty();
    }
}