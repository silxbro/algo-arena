package algo_arena.chat.controller;

import algo_arena.chat.dto.request.ChatSendRequest;
import algo_arena.chat.enums.MessageType;
import algo_arena.chat.factory.StringMessageFactory;
import algo_arena.chat.service.ChattingService;
import algo_arena.room.entity.Room;
import algo_arena.room.service.RoomFindService;
import algo_arena.utils.jwt.service.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ChatMessageController {

    private final ChattingService chattingService;
    private final RoomFindService roomFindService;
    private final StringMessageFactory stringMessageFactory;
    private final JwtTokenUtil jwtTokenUtil;

    @MessageMapping("/rooms/{roomId}/chat")
    public void chat(ChatSendRequest request,
        @DestinationVariable("roomId") final String roomId,
        @Header("type") MessageType type,
        @Header("token") String token) {

        String senderName = jwtTokenUtil.extractUsername(token);
        Room room = roomFindService.findRoomById(roomId);
        String message = stringMessageFactory.createStringMessage(type, room, senderName, request.getMessage());

        chattingService.send(type, room, senderName, message);
    }
}