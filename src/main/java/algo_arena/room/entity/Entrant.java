package algo_arena.room.entity;

import lombok.Getter;

@Getter
public class Entrant {

    private Long memberId;
    private Boolean isReady;

    public Entrant(Long memberId) {
        this.memberId = memberId;
        this.isReady = false;  // 초기값은 false
    }
}