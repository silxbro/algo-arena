package algo_arena.chat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import algo_arena.domain.chat.entity.ChatLog;
import algo_arena.domain.chat.enums.MessageType;
import algo_arena.domain.chat.repository.ChatLogRedisRepository;
import algo_arena.domain.chat.service.ChatLogService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ChatLogServiceTest {

    @InjectMocks
    ChatLogService chatLogService;

    @Mock
    ChatLogRedisRepository chatLogRepository;

    ChatLog chatLog;
    String roomId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roomId = "my-test-room";
        chatLog = ChatLog.create(MessageType.CHAT, "member", "hello");
    }

    @Test
    @DisplayName("채팅메시지의 내용을 수정할 수 있다")
    void updateMessage() {
        //given
        Long index = 0L;
        String newMessage = "message updated!";
        when(chatLogRepository.findOneByRoomIdAndIndex(roomId, index)).thenReturn(Optional.of(chatLog));

        //when
        chatLogService.updateMessage(roomId, index, newMessage);
        ChatLog updatedChatLog = chatLogService.findChatLogByRoomIdAndIndex(roomId, index);

        //then
        assertThat(updatedChatLog.getMessage()).isEqualTo(newMessage);
        verify(chatLogRepository, times(2)).findOneByRoomIdAndIndex(roomId, index);
        verify(chatLogRepository).setByRoomIdAndIndex(roomId, index, chatLog);
    }
}