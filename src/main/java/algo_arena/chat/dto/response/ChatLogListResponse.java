package algo_arena.chat.dto.response;

import algo_arena.chat.entity.ChatLog;
import java.util.List;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class ChatLogListResponse {

    private List<ChatLog> chatLogs;

}