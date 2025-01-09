package algo_arena.utils.pubsub;

import algo_arena.chat.dto.response.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // Redis에서 메시지를 받고 채팅방에 전달
            String messageBody = new String(message.getBody());
            ChatMessage chatMessage = objectMapper.readValue(messageBody, ChatMessage.class);
            messagingTemplate.convertAndSend("/sub/rooms/" + chatMessage.getRoomId(), chatMessage);
        } catch (Exception e) {
            log.error("error in sendMessage = {}", e.getMessage());
        }
    }
}