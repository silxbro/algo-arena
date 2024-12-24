package algo_arena.room.entity;

import lombok.Getter;

@Getter
public class Participant {

    private Long memberId;
    private Boolean isReady;

    public Participant(Long memberId) {
        this.memberId = memberId;
        this.isReady = false;  // 초기값은 false
    }
}