package algo_arena.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClientMessage {

    private String senderNickname;
    private String message;

}