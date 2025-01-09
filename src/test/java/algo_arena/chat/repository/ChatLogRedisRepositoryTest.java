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

    String roomId1 = "test-room-1";
    String roomId2 = "test-room-2";


    @Autowired
    ChatLogRedisRepository chatLogRepository;

    @AfterEach
    void clear() {
        chatLogRepository.deleteByRoomId(roomId1);
        chatLogRepository.deleteByRoomId(roomId2);
    }

    @Test
    @DisplayName("채팅이 채팅방별로 알맞게 저장되며, 해당 채팅방의 채팅목록을 조회할 수 있다")
    void saveAndFindAllByRoomId() {
        //given
        ChatLog chatLog1 = ChatLog.builder().message("hello!").senderName("member1").build();
        ChatLog chatLog2 = ChatLog.builder().message("안녕하세요!").senderName("member2").build();
        ChatLog chatLog3 = ChatLog.builder().message("반갑습니다^^").senderName("member3").build();

        //when
        chatLogRepository.saveByRoomId(roomId1, chatLog1);
        chatLogRepository.saveByRoomId(roomId1, chatLog2);
        chatLogRepository.saveByRoomId(roomId2, chatLog3);

        List<ChatLog> chatLogs1 = chatLogRepository.findAllByRoomId(roomId1);
        List<ChatLog> chatLogs2 = chatLogRepository.findAllByRoomId(roomId2);
        List<ChatLog> emptyLogs = chatLogRepository.findAllByRoomId("room");

        //then
        assertThat(chatLogs1).containsExactly(chatLog1, chatLog2);
        assertThat(chatLogs2).containsExactly(chatLog3);
        assertThat(emptyLogs).isEmpty();
    }

    @Test
    @DisplayName("채팅방 ID 및 Index 로 개별 채팅을 조회할 수 있다")
    void findOneByRoomIdAndIndex() {
        //given
        ChatLog chatLog1 = ChatLog.builder().message("hello!").senderName("member1").build();
        ChatLog chatLog2 = ChatLog.builder().message("안녕하세요!").senderName("member2").build();

        Long index1 = chatLogRepository.saveByRoomId(roomId1, chatLog1);
        Long index2 = chatLogRepository.saveByRoomId(roomId1, chatLog2);

        //when
        ChatLog findChatLog1 = chatLogRepository.findOneByRoomIdAndIndex(roomId1, index1).orElse(null);
        ChatLog findChatLog2 = chatLogRepository.findOneByRoomIdAndIndex(roomId1, index2).orElse(null);

        //then
        assertThat(index1).isEqualTo(0);
        assertThat(index2).isEqualTo(1);
        assertThat(findChatLog1).isEqualTo(chatLog1);
        assertThat(findChatLog2).isEqualTo(chatLog2);
    }

    @Test
    @DisplayName("채팅방 ID 및 index 로 채팅을 수정할 수 있다")
    void setByRoomIdAndIndex() {
        //given
        ChatLog chatLog = ChatLog.builder().message("hello!").senderName("member").build();
        Long index = chatLogRepository.saveByRoomId(roomId1, chatLog);

        ChatLog newChatLog = ChatLog.builder().message("안녕하세요!!!").senderName("new-member").build();

        //when
        chatLogRepository.setByRoomIdAndIndex(roomId1, index, newChatLog);
        ChatLog updatedChatLog = chatLogRepository.findOneByRoomIdAndIndex(roomId1, index).orElse(null);

        //then
        assertThat(updatedChatLog).isNotNull();
        assertThat(updatedChatLog.getMessage()).isEqualTo("안녕하세요!!!");
        assertThat(updatedChatLog.getSenderName()).isEqualTo("new-member");
    }

    @Test
    @DisplayName("채팅방 ID 로 채팅을 삭제할 수 있다")
    void deleteByRoomId() {
        //given
        ChatLog chatLog1 = ChatLog.builder().message("hello!").senderName("member1").build();
        ChatLog chatLog2 = ChatLog.builder().message("안녕하세요!").senderName("member2").build();

        chatLogRepository.saveByRoomId(roomId1, chatLog1);
        chatLogRepository.saveByRoomId(roomId1, chatLog2);

        //when
        chatLogRepository.deleteByRoomId(roomId1);
        List<ChatLog> chatLogs = chatLogRepository.findAllByRoomId(roomId1);

        //then
        assertThat(chatLogs).isEmpty();
    }
}