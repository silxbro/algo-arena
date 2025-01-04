package algo_arena.chat.controller;

import algo_arena.chat.dto.response.ChatLogListResponse;
import algo_arena.chat.entity.ChatLog;
import algo_arena.chat.service.ChatLogService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ChatLogApiController {

    private final ChatLogService chatLogService;

    @GetMapping("/room/{roomId}")
    public ResponseEntity<ChatLogListResponse> findChatLogs(@PathVariable("roomId") String roomId) {
        List<ChatLog> chatLogs = chatLogService.findChatLogsByRoomId(roomId);
        return ResponseEntity.ok(ChatLogListResponse.builder().chatLogs(chatLogs).build());
    }
}