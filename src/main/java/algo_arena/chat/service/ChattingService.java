package algo_arena.chat.service;

import algo_arena.chat.dto.response.ChatMessage;
import algo_arena.chat.dto.request.ChatSendRequest;
import algo_arena.chat.entity.ChatLog;
import algo_arena.chat.enums.ClientMessageType;
import algo_arena.chat.factory.ChatMessageFactory;
import algo_arena.member.entity.Member;
import algo_arena.member.service.MemberService;
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
    private final RedisTemplate<String, Object> redisTemplate;;
    private final RoomFindService roomFindService;
    private final ChatLogService chatLogService;
    private final MemberService memberService;
    private final ChatMessageFactory chatMessageFactory;

    /**
     * Room에 message 발송
     */
    public void send(ChatSendRequest request) {
        Room room = findRoom(request.getRoomId());
        Member sender = findSender(request.getSenderId());
        List<Member> receivers = findReceivers(room);
        ChatLog chatLog = createChatLog(request.getType(), request.getMessage(), sender.getNickname());
        Long index = chatLogService.saveChatLog(room.getId(), chatLog);

        ChatMessage chatMessage = chatMessageFactory.createMessage(
            room.getId(), index, request.getType(), request.getMessage(), sender, receivers);

        sendToRedis(chatMessage);
    }

    private Room findRoom(String roomId) {
        return roomFindService.findRoomById(roomId);
    }

    private Member findSender(Long senderId) {
        return memberService.findMemberById(senderId);
    }

    private List<Member> findReceivers(Room room) {
        return room.getRoomMembers().stream().map(RoomMember::getMember).toList();
    }

    private ChatLog createChatLog(ClientMessageType type, String message, String senderNickname) {
        return ChatLog.create(type, message, senderNickname);
    }

    private void sendToRedis(ChatMessage chatMessage) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }
}