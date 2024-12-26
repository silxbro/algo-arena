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

    public void update(Room updateInfo) {
        this.name = updateInfo.getName();
        this.maxEntrants = updateInfo.getMaxEntrants();
        this.problemIds = updateInfo.getProblemIds();
        this.language = updateInfo.getLanguage();
        this.timeLimit = updateInfo.getTimeLimit();
    }

    public void changeHost() {
        Entrant forHost = entrants.remove(0);
        hostId = forHost.getMemberId();
    }

    public boolean addEntrant(Long memberId) {
        //TODO: 이미 소속된 테스트방이 존재하는 경우: 예외처리 / 불가능한 경우인데, 예외처리가 필요할까?
        if (isFull()) {
            return false;
        }
        entrants.add(new Entrant(memberId));
        return true;
    }

    public void removeEntrant(Long memberId) {
        entrants.removeIf(entrant -> containsEntrant(entrant.getMemberId()));
    }

    public boolean isHost(Long memberId) {
        return memberId.equals(hostId);
    }

    public boolean hasNoEntrants() {
        return entrants.isEmpty();
    }

    public boolean isFull() {
        return entrants.size() == maxEntrants;
    }

    public boolean containsEntrant(Long memberId) {
        return entrants.stream()
            .anyMatch(entrant -> memberId.equals(entrant.getMemberId()));
    }
}