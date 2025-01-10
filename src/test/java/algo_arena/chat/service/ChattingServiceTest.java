package algo_arena.chat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import algo_arena.chat.dto.response.ChatMessage;
import algo_arena.chat.entity.ChatLog;
import algo_arena.chat.enums.MessageType;
import algo_arena.chat.factory.ChatMessageFactory;
import algo_arena.room.entity.Room;
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

    MessageType type = MessageType.CHAT;
    String roomId = "chatRoom";
    String senderName = "sender";
    String message = "hello!";
    Long chatLogIndex = 0L;


    @InjectMocks
    ChattingService chattingService;

    @Mock
    ChannelTopic channelTopic;

    @Mock
    RedisTemplate<String, Object> redisTemplate;

    @Mock
    ChatMessageFactory chatMessageFactory;

    @Mock
    ChatLogService chatLogService;

    @Mock
    ChatMessage mockChatMessage;

    @Mock
    Room room;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(channelTopic.getTopic()).thenReturn("topicName");
    }

    @Test
    @DisplayName("채팅 메시지를 Redis 에 발행하고 로그를 저장한다")
    void send_Success() {
        //given
        when(room.getId()).thenReturn(roomId);
        when(chatLogService.saveChatLog(eq(roomId), any(ChatLog.class))).thenReturn(chatLogIndex);
        when(chatMessageFactory.createMessage(type, roomId, chatLogIndex, senderName, message))
            .thenReturn(mockChatMessage);

        //when
        chattingService.send(type, room, senderName, message);

        //then
        //ChatLog 저장 호출 확인
        ArgumentCaptor<ChatLog> chatLogCaptor = ArgumentCaptor.forClass(ChatLog.class);
        verify(chatLogService).saveChatLog(eq(roomId), chatLogCaptor.capture());
        ChatLog capturedChatLog = chatLogCaptor.getValue();
        assertThat(capturedChatLog.getType()).isEqualTo(type);
        assertThat(capturedChatLog.getSenderName()).isEqualTo(senderName);
        assertThat(capturedChatLog.getMessage()).isEqualTo(message);

        //ChatMessage 생성 확인
        verify(chatMessageFactory).createMessage(type, roomId, chatLogIndex, senderName, message);

        //Redis 발행 확인
        verify(redisTemplate).convertAndSend(eq(channelTopic.getTopic()), eq(mockChatMessage));
    }
}