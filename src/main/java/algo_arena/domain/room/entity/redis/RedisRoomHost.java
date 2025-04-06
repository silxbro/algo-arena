package algo_arena.domain.room.entity.redis;

import algo_arena.domain.member.entity.Member;
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
@RedisHash("RoomHost")
public class RedisRoomHost {

    private String name;

    public static RedisRoomHost from(Member host) {
        return RedisRoomHost.builder()
            .name(host.getName())
            .build();
    }
}