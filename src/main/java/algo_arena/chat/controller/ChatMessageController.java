package algo_arena.chat.controller;

import algo_arena.chat.dto.request.ClientMessage;
import algo_arena.chat.enums.ClientMessageType;
import algo_arena.chat.factory.SocketMessageFactory;
import algo_arena.chat.service.ChattingService;
import algo_arena.member.entity.Member;
import algo_arena.member.service.MemberService;
import algo_arena.utils.jwt.service.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ChatMessageController {

    private final ChattingService chattingService;
    private final MemberService memberService;
    private final SocketMessageFactory socketMessageFactory;
    private final JwtTokenUtil jwtTokenUtil;

    @MessageMapping("/rooms/{roomId}/chat")
    public void chat(@Payload ClientMessage message,
        @DestinationVariable final String roomId,
        @Header("type") ClientMessageType type,
        @Header("token") String token) {

        String username = jwtTokenUtil.extractUsername(token);
        Member member = memberService.findMemberByNickname(username);

        socketMessageFactory.insertMessage(message, type, roomId, member);

        chattingService.send(message.getMessage(), type, roomId, member);
    }
}