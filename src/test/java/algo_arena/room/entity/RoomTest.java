package algo_arena.room.entity;

import static org.assertj.core.api.Assertions.assertThat;

import algo_arena.member.entity.Member;
import algo_arena.room.dto.request.RoomUpdateRequest;
import algo_arena.submission.entity.Language;
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
            .maxRoomMembers(3)
            .language(Language.KOTLIN)
            .timeLimit(60)
            .build();
        room.initHost(createTestMember(1L));
    }

    @Test
    @DisplayName("테스트방 정보를 변경할 수 있다")
    void update() {
        //given
        RoomUpdateRequest request = RoomUpdateRequest.builder()
            .name("Coding Test")
            .maxRoomMembers(20)
            .languageName("C++")
            .timeLimit(30)
            .build();

        //when
        room.update(request.toEntity());

        //then
        assertThat(room.getName()).isEqualTo("Coding Test");
        assertThat(room.getMaxRoomMembers()).isEqualTo(20);
        assertThat(room.getLanguage()).isEqualTo(Language.C_PP);
        assertThat(room.getTimeLimit()).isEqualTo(30);
    }

    @Test
    @DisplayName("참가자 중 가장 먼저 입장한 사람이 자동으로 방장으로 변경된다")
    void changeHost() {
        //given
        room.addMember(createTestMember(2L));
        room.addMember(createTestMember(3L));
        room.addMember(createTestMember(4L));

        //when
        Member changedHost = room.changeHost();

        //then
        assertThat(changedHost.getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("테스트방에 참가자가 입장할 수 있다")
    void addMember_success() {
        //given
        room.addMember(createTestMember(100L));
        room.addMember(createTestMember(200L));

        //when
        List<RoomMember> roomMembers = room.getRoomMembers();

        //then
        assertThat(roomMembers.size()).isEqualTo(2);
        assertThat(room.isMember(100L)).isTrue();
        assertThat(room.isMember(200L)).isTrue();
    }

    @Test
    @DisplayName("정원이 최대정원만큼 차있는 경우, 테스트방에 입장할 수 없다")
    void addMember_fail() {
        //given
        boolean result1 = room.addMember(createTestMember(100L));
        boolean result2 = room.addMember(createTestMember(200L));
        boolean result3 = room.addMember(createTestMember(300L));
        boolean result4 = room.addMember(createTestMember(400L));

        //when
        List<RoomMember> roomMembers = room.getRoomMembers();

        //then
        assertThat(roomMembers.size()).isEqualTo(room.getMaxRoomMembers());
        assertThat(result1).isTrue();
        assertThat(result2).isTrue();
        assertThat(result3).isTrue();
        assertThat(result4).isFalse();
    }

    @Test
    @DisplayName("테스트방에서 참가자가 퇴장할 수 있다")
    void removeMember() {
        //given
        room.addMember(createTestMember(100L));
        room.addMember(createTestMember(200L));

        //when
        room.removeMember(200L);
        List<RoomMember> roomMembers = room.getRoomMembers();

        //then
        assertThat(roomMembers.size()).isEqualTo(1);
        assertThat(room.isMember(100L)).isTrue();
        assertThat(room.isMember(200L)).isFalse();
    }

    @Test
    @DisplayName("테스트방에 참가자가 없을 경우, 이를 조회할 수 있다")
    void existMembers_false() {
        //given

        //when
        boolean result = room.existMembers();

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("테스트방에 참가자가 존재할 경우, 이를 조회할 수 있다")
    void existMembers_true() {
        //given
        room.addMember(createTestMember(1L));

        //when
        boolean result = room.existMembers();

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("테스트방에 참가자가 최대 정원보다 적은 경우, 이를 조회할 수 있다")
    void isFull_no() {
        //given
        room.addMember(createTestMember(1L));

        //when
        boolean result = room.isFull();

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("테스트방에 참가자가 최대 정원인 경우, 이를 조회할 수 있다")
    void isFull_yes() {
        //given
        room.addMember(createTestMember(1L));
        room.addMember(createTestMember(2L));
        room.addMember(createTestMember(3L));

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
    void isMember() {
        //given
        room.addMember(createTestMember(1L));
        room.addMember(createTestMember(2L));

        //when
        boolean result1 = room.isMember(1L);
        boolean result2 = room.isMember(10L);

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }

    private Member createTestMember(Long memberId) {
        return Member.builder().id(memberId).build();
    }
}