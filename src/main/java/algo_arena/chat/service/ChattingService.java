package algo_arena.chat.service;

import algo_arena.chat.dto.response.ChatMessage;
import algo_arena.chat.entity.ChatLog;
import algo_arena.chat.enums.ClientMessageType;
import algo_arena.chat.factory.ChatMessageFactory;
import algo_arena.member.entity.Member;
import algo_arena.room.entity.Room;
import algo_arena.room.entity.RoomMember;
import algo_arena.room.service.RoomFindService;
import java.util.List;
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

    private final RoomFindService roomFindService;
    private final ChatLogService chatLogService;

    /**
     * Room 에 message 발송
     */
    public void send(String message, ClientMessageType type, String roomId, Member member) {
        Room room = findRoom(roomId);
        List<Member> receivers = findReceivers(room);
        Long index = saveChatLog(type, message, member.getNickname(), roomId);

        ChatMessage chatMessage = chatMessageFactory.createMessage(
            room.getId(), index, type, message, member, receivers);

        sendToRedis(chatMessage);
    }

    private Room findRoom(String roomId) {
        return roomFindService.findRoomById(roomId);
    }

    private List<Member> findReceivers(Room room) {
        return room.getRoomMembers().stream().map(RoomMember::getMember).toList();
    }

    private Long saveChatLog(ClientMessageType type, String message, String senderName, String roomId) {
        ChatLog chatLog = ChatLog.create(type, message, senderName);
        return chatLogService.saveChatLog(roomId, chatLog);
    }

    private void sendToRedis(ChatMessage chatMessage) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }
}