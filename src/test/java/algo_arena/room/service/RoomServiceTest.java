package algo_arena.room.service;

import static algo_arena.room.service.RoomUpdateResult.State.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import algo_arena.member.entity.Member;
import algo_arena.member.service.MemberService;
import algo_arena.room.dto.request.RoomUpdateRequest;
import algo_arena.room.entity.Room;
import algo_arena.room.repository.RoomRedisRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RoomServiceTest {

    @Mock
    private RoomRedisRepository roomRepository;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private RoomService roomService;

    Room room;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        room = Room.builder().maxEntrants(3).hostId(1L).build();
    }

    @Test
    @DisplayName("새로운 테스트방을 성공적으로 생성할 수 있다")
    void createRoom() {
        //given
        when(roomRepository.save(room)).thenReturn(room);

        //when
        Room createdRoom = roomService.create(room);

        //then
        assertThat(createdRoom).isEqualTo(room);
        verify(roomRepository).save(room);
    }

    @Test
    @DisplayName("ID로 테스트방을 조회하면 해당 테스트방을 정상적으로 반환한다")
    void findRoomById_Found() {
        //given
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        //when
        Room findRoom = roomService.findOneById(room.getId());

        //then
        assertThat(findRoom).isEqualTo(room);
        verify(roomRepository).findById(room.getId());
    }

    @Test
    @DisplayName("ID로 테스트방을 조회할 때, 해당 테스트방이 없을 경우 Runtime 예외가 발생한다")
    void findRoomById_NotFound() {
        //given
        when(roomRepository.findById(any(String.class))).thenReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> roomService.findOneById("notAvailableId"))
            .isInstanceOf(RuntimeException.class);
        verify(roomRepository).findById(any(String.class));
    }

    @Test
    @DisplayName("테스트방 정보를 성공적으로 업데이트(수정)할 수 있다")
    void updateRoom() {
        //given
        RoomUpdateRequest request = RoomUpdateRequest.builder().build();
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        //when
        RoomUpdateResult result = roomService.update(room.getId(), request.toEntity());

        //then
        assertThat(result.getState()).isEqualTo(ROOM_UPDATED);
        assertThat(result.getMessage()).isEqualTo("테스트방 변경이 완료되었습니다.");
        verify(roomRepository).findById(room.getId());
    }

    @Test
    @DisplayName("테스트방의 인원이 최대인원보다 적을 경우, 참가자가 성공적으로 입장할 수 있다")
    void enterRoom_Success() {
        //given
        Member member = mock(Member.class);
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        when(memberService.findOneById(member.getId())).thenReturn(member);

        //when
        RoomUpdateResult result = roomService.enter(room.getId(), member.getId());

        //then
        assertThat(result.getState()).isEqualTo(ENTRANT_ENTERED);
        assertThat(result.getMessage()).isEqualTo(String.format("[%s]님이 입장했습니다.", member.getNickname()));
        verify(roomRepository).findById(room.getId());
    }

    @Test
    @DisplayName("테스트방의 인원이 최대인원과 같을 경우, 참가자는 입장할 수 없다")
    void enterRoom_FullRoom() {
        //given
        Member member = mock(Member.class);
        //3명(최대인원) 추가
        room.addEntrant(1L);
        room.addEntrant(2L);
        room.addEntrant(3L);
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        when(memberService.findOneById(member.getId())).thenReturn(member);

        //when
        RoomUpdateResult result = roomService.enter(room.getId(), member.getId());

        //then
        assertThat(result.getState()).isEqualTo(FULL_ROOM);
        assertThat(result.getMessage()).isEqualTo("정원이 초과되어 입장할 수 없습니다.");
        verify(roomRepository).findById(room.getId());
    }

    @Test
    @DisplayName("참가자가 없는 상태에서 방장이 퇴장할 경우, 해당 방이 삭제된다")
    void exitRoom_DeletesRoomIfHostAndEmpty() {
        //given
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        //when
        RoomUpdateResult result = roomService.exit(room.getId(), room.getHostId());

        //then
        assertThat(result.getState()).isEqualTo(ROOM_DELETED);
        assertThat(result.getMessage()).isEqualTo("테스트방이 삭제되었습니다.");
        verify(roomRepository).findById(room.getId());
        verify(roomRepository).deleteById(room.getId());
    }

    @Test
    @DisplayName("참가자가 있는 상태에서 방장이 퇴장할 경우, 가장 먼저 입장한 참가자가 방장이 된다")
    void exitRoom_ChangeHostIfHostExit() {
        //given
        room.addEntrant(2L);
        room.addEntrant(3L);
        room.addEntrant(4L);
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        when(memberService.findOneById(2L)).thenReturn(Member.builder().nickname("first-member").build());

        //when
        RoomUpdateResult result = roomService.exit(room.getId(), room.getHostId());

        //then
        assertThat(room.getEntrants().size()).isEqualTo(2);
        assertThat(result.getState()).isEqualTo(HOST_CHANGED);
        assertThat(result.getMessage()).isEqualTo(String.format("방장이 [%s]님으로 변경되었습니다.", "first-member"));
        verify(roomRepository).findById(room.getId());
        verify(memberService).findOneById(2L);
    }

    @Test
    @DisplayName("참가자가 테스트방에서 퇴장할 수 있다")
    void exitRoom_General() {
        //given
        room.addEntrant(2L);
        room.addEntrant(3L);
        room.addEntrant(4L);
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        when(memberService.findOneById(3L)).thenReturn(Member.builder().nickname("exit-member").build());

        //when
        RoomUpdateResult result = roomService.exit(room.getId(), 3L);

        //then
        assertThat(room.getEntrants().size()).isEqualTo(2);
        assertThat(result.getState()).isEqualTo(ENTRANT_EXITED);
        assertThat(result.getMessage()).isEqualTo(String.format("[%s]님이 퇴장했습니다.", "exit-member"));
        verify(roomRepository).findById(room.getId());
        verify(memberService).findOneById(3L);
    }

    @Test
    @DisplayName("ID로 테스트방을 삭제하면 해당 테스트방이 정상적으로 삭제되며, 삭제된 방에 접근하면 예외가 발생한다")
    void deleteRoom() {
        //given
        doNothing().when(roomRepository).deleteById(room.getId());

        //when
        roomService.delete(room.getId());

        //then
        assertThatThrownBy(() -> roomService.findOneById(room.getId()))
            .isInstanceOf(NoSuchElementException.class);
        verify(roomRepository).deleteById(room.getId());
    }
}