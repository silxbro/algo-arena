package algo_arena.chat.controller;

import algo_arena.chat.dto.request.ClientMessage;
import algo_arena.chat.enums.ClientMessageType;
import algo_arena.chat.factory.SocketMessageFactory;
import algo_arena.chat.service.ChattingService;
import algo_arena.member.entity.Member;
import algo_arena.member.service.MemberService;
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

    @MessageMapping("/rooms/{roomId}/chat")
    public void chat(@Payload ClientMessage message,
        @DestinationVariable final String roomId,
        @Header("type") ClientMessageType type) {

        Long memberId = 1L;
        Member member = memberService.findMemberById(memberId);
        if (type.isChat() || type.isEnter() || type.isExit()) {
            socketMessageFactory.updateMessage(roomId, type, message, member);
        }
        ChatSendRequest chatSendRequest = createChatSendRequest(message, roomId, type, memberId);
        chattingService.send(chatSendRequest);
    }

    private ChatSendRequest createChatSendRequest(
        ClientMessage message, String roomId, ClientMessageType type, Long memberId) {

        return ChatSendRequest.builder()
            .roomId(roomId)
            .type(type)
            .message(message.getMessage())
            .senderId(memberId)
            .build();
    }
}