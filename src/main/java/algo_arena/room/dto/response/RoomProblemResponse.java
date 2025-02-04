package algo_arena.room.dto.response;

import algo_arena.room.entity.RoomProblem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomProblemResponse {
    private Long number;
    private String title;
    private String link;

    public static RoomProblemResponse from(RoomProblem roomProblem) {
        return RoomProblemResponse.builder()
            .number(roomProblem.getProblem().getNumber())
            .title(roomProblem.getProblem().getTitle())
            .link(roomProblem.getProblem().getLink())
            .build();
    }
}