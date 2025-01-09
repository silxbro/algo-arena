package algo_arena.chat.service;

import algo_arena.chat.entity.ChatLog;
import algo_arena.chat.repository.ChatLogRedisRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatLogService {

    private final ChatLogRedisRepository chatLogRepository;

    @Transactional
    public Long saveChatLog(String roomId, ChatLog chatLog) {
        return chatLogRepository.saveByRoomId(roomId, chatLog);
    }

    public ChatLog findChatLogByRoomIdAndIndex(String roomId, Long index) {
        return chatLogRepository.findOneByRoomIdAndIndex(roomId, index).orElseThrow();
    }

    public List<ChatLog> findChatLogsByRoomId(String roomId) {
        return chatLogRepository.findAllByRoomId(roomId);
    }

    @Transactional
    public void updateMessage(String roomId, Long index, String newMessage) {
        ChatLog chatLog = findChatLogByRoomIdAndIndex(roomId, index);
        if (chatLog != null) {
            chatLog.updateMessage(newMessage);
            chatLogRepository.setByRoomIdAndIndex(roomId, index, chatLog);
        }
    }

    @Transactional
    public void deleteMessage(String roomId, Long index) {
        updateMessage(roomId, index, "삭제된 메시지입니다.");
    }
}