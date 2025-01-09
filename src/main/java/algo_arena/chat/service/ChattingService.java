package algo_arena.chat.service;

import algo_arena.chat.dto.response.ChatMessage;
import algo_arena.chat.entity.ChatLog;
import algo_arena.chat.enums.MessageType;
import algo_arena.chat.factory.ChatMessageFactory;
import algo_arena.room.entity.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChattingService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatMessageFactory chatMessageFactory;
    private final ChatLogService chatLogService;

    /**
     * Room 에 message 발송
     */
    public void send(MessageType type, Room room, String senderName, String message) {
        Long index = saveChatLog(type, room.getId(), senderName, message);

        ChatMessage chatMessage = chatMessageFactory.createMessage(
            type, room.getId(), index, senderName, message);

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