package algo_arena.room.entity;

import algo_arena.submission.entity.Language;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash("room")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Room implements Serializable {

    @Id
    @EqualsAndHashCode.Include
    private String id = UUID.randomUUID().toString();

    private String name;
    private Integer maxEntrants;
    private List<Long> problemIds;
    private Long hostId;
    private List<Entrant> entrants = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Language language;

    private Integer timeLimit; //분 단위

    @TimeToLive
    private Long ttl;

    @Builder
    public Room(String name, Integer maxEntrants, List<Long> problemIds, Long hostId, Language language, Integer timeLimit, Long ttl) {
        this.name = name;
        this.maxEntrants = maxEntrants;
        this.problemIds = problemIds;
        this.hostId = hostId;
        this.language = language;
        this.timeLimit = timeLimit;
        this.ttl = ttl;
    }
}