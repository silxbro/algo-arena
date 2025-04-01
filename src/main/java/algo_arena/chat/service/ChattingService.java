package algo_arena.chat.service;

import algo_arena.chat.dto.ChatMessage;
import algo_arena.chat.entity.ChatLog;
import algo_arena.chat.enums.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChattingService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatLogService chatLogService;

    /**
     * Room 에 message 발송
     */
    public void send(MessageType type, String roomId, String senderName, String message) {
        Long index = saveChatLog(type, roomId, senderName, message);

        ChatMessage chatMessage = ChatMessage.create(type, roomId, index, senderName, message);

        sendToRedis(chatMessage);
    }

    private Long saveChatLog(MessageType type, String roomId, String senderName, String message) {
        ChatLog chatLog = ChatLog.create(type, senderName, message);
        return chatLogService.saveChatLog(roomId, chatLog);
    }

    private void sendToRedis(ChatMessage chatMessage) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }
}