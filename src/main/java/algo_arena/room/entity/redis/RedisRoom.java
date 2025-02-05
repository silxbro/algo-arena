package algo_arena.room.entity.redis;

import algo_arena.member.entity.Member;
import algo_arena.problem.entity.Problem;
import algo_arena.room.entity.Room;
import algo_arena.room.entity.RoomMember;
import algo_arena.room.entity.RoomProblem;
import algo_arena.submission.enums.Language;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.List;
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
@RedisHash("Room")
public class RedisRoom implements Serializable {

    @Id
    private String id;

    private String name;
    private Integer maxRoomMembers;
    private List<RedisRoomProblem> roomProblems;
    private RedisRoomHost roomHost;
    private List<RedisRoomMember> roomMembers;
    private Language language;
    private Integer timeLimit; // 분 단위

    public static RedisRoom from(Room room) {
        return RedisRoom.builder()
            .id(room.getId())
            .name(room.getName())
            .maxRoomMembers(room.getMaxRoomMembers())
            .roomProblems(room.getRoomProblems().stream().map(RedisRoomProblem::from).toList())
            .roomHost(RedisRoomHost.from(room.getHost()))
            .roomMembers(room.getRoomMembers().stream().map(RedisRoomMember::from).toList())
            .language(room.getLanguage())
            .timeLimit(room.getTimeLimit())
            .build();
    }

    public static Room toRoom(RedisRoom redisRoom) {

        List<RoomProblem> roomProblems = redisRoom.getRoomProblems()
            .stream().map(each ->
                RoomProblem.builder()
                    .problem(Problem.builder().number(each.getNumber()).title(each.getTitle()).link(each.getLink()).build())
                    .build()
            )
            .toList();

        List<RoomMember> roomMembers = redisRoom.getRoomMembers()
            .stream().map(each ->
                RoomMember.builder()
                    .member(Member.builder().name(each.getName()).build())
                    .isReady(each.getIsReady())
                    .build()
            )
            .toList();

        RedisRoomHost redisRoomHost = redisRoom.getRoomHost();
        Member host = Member.builder().name(redisRoomHost.getName()).build();

        return Room.builder()
            .id(redisRoom.getId())
            .name(redisRoom.getName())
            .maxRoomMembers(redisRoom.getMaxRoomMembers())
            .roomProblems(roomProblems)
            .host(host)
            .roomMembers(roomMembers)
            .language(redisRoom.getLanguage())
            .timeLimit(redisRoom.getTimeLimit())
            .build();
    }
}