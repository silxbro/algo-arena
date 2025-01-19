package algo_arena.room.entity.redis;

import algo_arena.problem.entity.Problem;
import algo_arena.room.entity.RoomProblem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@RedisHash("RoomProblem")
public class RedisRoomProblem {

    private Long number;
    private String title;
    private String link;

    public static RedisRoomProblem from(RoomProblem roomProblem) {
        Problem problem = roomProblem.getProblem();
        return RedisRoomProblem.builder()
            .number(problem.getNumber())
            .title(problem.getTitle())
            .link(problem.getLink())
            .build();
    }
}