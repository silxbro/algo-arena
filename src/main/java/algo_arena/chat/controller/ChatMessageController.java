package algo_arena.chat.controller;

import algo_arena.chat.dto.request.ChatSendRequest;
import algo_arena.chat.enums.MessageType;
import algo_arena.chat.factory.SystemMessageFactory;
import algo_arena.chat.service.ChattingService;
import algo_arena.room.entity.Room;
import algo_arena.room.service.RoomFindService;
import algo_arena.utils.jwt.service.JwtTokenUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ChatMessageController {

    private final ChattingService chattingService;
    private final RoomFindService roomFindService;
    private final SystemMessageFactory systemMessageFactory;
    private final JwtTokenUtil jwtTokenUtil;

    @MessageMapping("/rooms/{roomId}/chat")
    public void chat(@Valid @RequestBody ChatSendRequest request, @DestinationVariable("roomId") String roomId,
        @Header("type") MessageType type, @Header("token") String token) {

        String senderName = jwtTokenUtil.extractUsername(token);
        Room room = roomFindService.findRoomById(roomId, senderName);
        String message = getMessageByType(type, room, senderName, request.getMessage());

        chattingService.send(type, room.getId(), senderName, message);
    }

    private String getMessageByType(MessageType type, Room room, String senderName, String message) {
        if (type.isChatType()) {
            return message;
        }
        return systemMessageFactory.create(type, room, senderName);
    }
}