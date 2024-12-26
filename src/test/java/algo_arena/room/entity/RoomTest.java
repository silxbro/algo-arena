package algo_arena.room.entity;

import static org.assertj.core.api.Assertions.assertThat;

import algo_arena.room.dto.request.RoomUpdateRequest;
import algo_arena.submission.entity.Language;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoomTest {

    Room room;

    @BeforeEach
    void init() {
        room = Room.builder()
            .name("코딩테스트")
            .maxEntrants(3)
            .problemIds(Arrays.asList(1L, 2L, 3L))
            .hostId(1L)
            .language(Language.KOTLIN)
            .timeLimit(60)
            .build();
    }

    @Test
    @DisplayName("테스트방 정보를 변경할 수 있다")
    void update() {
        //given
        RoomUpdateRequest request = RoomUpdateRequest.builder()
            .name("Coding Test")
            .maxEntrants(20)
            .problemIds(Arrays.asList(2L, 3L))
            .languageName("C++")
            .timeLimit(30)
            .build();

        //when
        room.update(request.toEntity());

        //then
        assertThat(room.getName()).isEqualTo("Coding Test");
        assertThat(room.getMaxEntrants()).isEqualTo(20);
        assertThat(room.getProblemIds()).isEqualTo(Arrays.asList(2L, 3L));
        assertThat(room.getLanguage()).isEqualTo(Language.C_PP);
        assertThat(room.getTimeLimit()).isEqualTo(30);
    }

    @Test
    @DisplayName("참가자 중 가장 먼저 입장한 사람이 자동으로 방장으로 변경된다")
    void changeHost() {
        //given
        room.addEntrant(2L);
        room.addEntrant(3L);
        room.addEntrant(4L);

        //when
        room.changeHost();

        //then
        assertThat(room.getHostId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("테스트방에 참가자가 입장할 수 있다")
    void addEntrant_success() {
        //given
        room.addEntrant(100L);
        room.addEntrant(200L);

        //when
        List<Entrant> entrants = room.getEntrants();

        //then
        assertThat(entrants.size()).isEqualTo(2);
        assertThat(room.isEntrant(100L)).isTrue();
        assertThat(room.isEntrant(200L)).isTrue();
    }

    @Test
    @DisplayName("정원이 최대정원만큼 차있는 경우, 테스트방에 입장할 수 없다")
    void addEntrant_fail() {
        //given
        boolean result1 = room.addEntrant(100L);
        boolean result2 = room.addEntrant(200L);
        boolean result3 = room.addEntrant(300L);
        boolean result4 = room.addEntrant(400L);

        //when
        List<Entrant> entrants = room.getEntrants();

        //then
        assertThat(entrants.size()).isEqualTo(room.getMaxEntrants());
        assertThat(result1).isTrue();
        assertThat(result2).isTrue();
        assertThat(result3).isTrue();
        assertThat(result4).isFalse();
    }

    @Test
    @DisplayName("테스트방에서 참가자가 퇴장할 수 있다")
    void removeEntrant() {
        //given
        room.addEntrant(100L);
        room.addEntrant(200L);

        //when
        room.removeEntrant(200L);
        List<Entrant> entrants = room.getEntrants();

        //then
        assertThat(entrants.size()).isEqualTo(1);
        assertThat(room.isEntrant(100L)).isTrue();
        assertThat(room.isEntrant(200L)).isFalse();
    }

    @Test
    @DisplayName("테스트방에 참가자가 없을 경우, 이를 조회할 수 있다")
    void hasEntrants_none() {
        //given

        //when
        boolean result = room.hasEntrants();

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("테스트방에 참가자가 존재할 경우, 이를 조회할 수 있다")
    void hasEntrants_exist() {
        //given
        room.addEntrant(1L);

        //when
        boolean result = room.hasEntrants();

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("테스트방에 참가자가 최대 정원보다 적은 경우, 이를 조회할 수 있다")
    void isFull_no() {
        //given
        room.addEntrant(1L);

        //when
        boolean result = room.isFull();

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("테스트방에 참가자가 최대 정원인 경우, 이를 조회할 수 있다")
    void isFull_yes() {
        //given
        room.addEntrant(1L);
        room.addEntrant(2L);
        room.addEntrant(3L);

        //when
        boolean result = room.isFull();

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("회원 ID로 테스트방의 방장여부를 조회할 수 있다")
    void isHost() {
        //given

        //when
        boolean result1 = room.isHost(1L);
        boolean result2 = room.isHost(100L);

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }

    @Test
    @DisplayName("회원 ID로 테스트방의 참가자여부를 조회할 수 있다")
    void isEntrant() {
        //given
        room.addEntrant(1L);
        room.addEntrant(2L);

        //when
        boolean result1 = room.isEntrant(1L);
        boolean result2 = room.isEntrant(10L);

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }
}