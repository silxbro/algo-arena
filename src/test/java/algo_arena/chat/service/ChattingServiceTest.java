package algo_arena.chat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import algo_arena.domain.chat.dto.ChatMessage;
import algo_arena.domain.chat.entity.ChatLog;
import algo_arena.domain.chat.enums.MessageType;
import algo_arena.domain.chat.service.ChatLogService;
import algo_arena.domain.chat.service.ChattingService;
import algo_arena.domain.room.entity.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

class ChattingServiceTest {

    @InjectMocks
    ChattingService chattingService;

    @Mock
    ChannelTopic channelTopic;

    @Mock
    RedisTemplate<String, Object> redisTemplate;

    @Mock
    ChatLogService chatLogService;

    MessageType type = MessageType.CHAT;
    String roomId = "chatRoom";
    String senderName = "sender";
    String message = "hello!";
    Long chatLogIndex = 0L;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(channelTopic.getTopic()).thenReturn("topicName");
    }

    @Test
    @DisplayName("채팅 메시지를 Redis 에 발행하고 로그를 저장한다")
    void send_Success() {
        //given
        Room room = Room.builder().id(roomId).build();
        when(chatLogService.saveChatLog(eq(roomId), any(ChatLog.class))).thenReturn(chatLogIndex);

        //when
        chattingService.send(type, room, senderName, message);

        //then
        //ChatLog 검증
        ArgumentCaptor<ChatLog> chatLogCaptor = ArgumentCaptor.forClass(ChatLog.class);
        verify(chatLogService).saveChatLog(eq(roomId), chatLogCaptor.capture());
        ChatLog capturedChatLog = chatLogCaptor.getValue();
        assertThat(capturedChatLog.getType()).isEqualTo(type);
        assertThat(capturedChatLog.getSenderName()).isEqualTo(senderName);
        assertThat(capturedChatLog.getMessage()).isEqualTo(message);

        //Redis 메시지 검증
        ArgumentCaptor<ChatMessage> messageCaptor = ArgumentCaptor.forClass(ChatMessage.class);
        verify(redisTemplate).convertAndSend(eq(channelTopic.getTopic()), messageCaptor.capture());
        ChatMessage capturedMessage = messageCaptor.getValue();
        assertThat(capturedMessage.getType()).isEqualTo(type);
        assertThat(capturedMessage.getRoomId()).isEqualTo(roomId);
        assertThat(capturedMessage.getSenderName()).isEqualTo(senderName);
        assertThat(capturedMessage.getMessage()).isEqualTo(message);
        assertThat(capturedMessage.getIndex()).isEqualTo(chatLogIndex);
    }
}