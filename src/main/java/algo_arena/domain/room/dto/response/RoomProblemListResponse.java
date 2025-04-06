package algo_arena.domain.room.dto.response;

import algo_arena.domain.room.entity.RoomProblem;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomProblemListResponse {
    private Integer number;
    private List<RoomProblemResponse> roomProblems;

    public static RoomProblemListResponse from(List<RoomProblem> roomProblems) {
        List<RoomProblemResponse> problems = roomProblems.stream().map(RoomProblemResponse::from).toList();
        return RoomProblemListResponse.builder().number(problems.size()).roomProblems(problems).build();
    }
}