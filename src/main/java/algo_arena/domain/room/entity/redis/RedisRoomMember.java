package algo_arena.domain.room.entity.redis;

import algo_arena.domain.member.entity.Member;
import algo_arena.domain.room.entity.RoomMember;
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
@RedisHash("RoomMember")
public class RedisRoomMember {

    private String name;
    private Boolean isReady;

    public static RedisRoomMember from(RoomMember roomMember) {
        Member member = roomMember.getMember();
        return RedisRoomMember.builder()
            .name(member.getName())
            .build();
    }
}