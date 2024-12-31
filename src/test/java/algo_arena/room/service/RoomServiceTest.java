package algo_arena.room.service;

import static algo_arena.room.service.RoomUpdateResult.State.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import algo_arena.member.entity.Member;
import algo_arena.member.service.MemberService;
import algo_arena.problem.entity.Problem;
import algo_arena.problem.repository.ProblemRepository;
import algo_arena.room.dto.request.RoomCreateRequest;
import algo_arena.room.dto.request.RoomUpdateRequest;
import algo_arena.room.entity.Room;
import algo_arena.room.repository.RoomRedisRepository;
import algo_arena.room.repository.RoomRepository;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RoomServiceTest {

    @InjectMocks
    RoomService roomService;

    @Mock
    RoomRepository roomRepository;
    @Mock
    RoomRedisRepository roomRedisRepository;
    @Mock
    ProblemRepository problemRepository;
    @Mock
    MemberService memberService;

    Member host;
    Room room;
    List<Long> problemIds;
    List<Problem> problems;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        host = Member.builder().id(1L).nickname("host").build();
        room = Room.builder().maxRoomMembers(3).host(host).build();
        problemIds = Arrays.asList(1L, 2L);
        problems = Arrays.asList(Problem.builder().id(1L).build(), Problem.builder().id(2L).build());
    }

    @Test
    @DisplayName("새로운 테스트방을 성공적으로 생성할 수 있다")
    void createRoom() {
        //given
        RoomCreateRequest request = RoomCreateRequest.builder().name("test-room").problemIds(problemIds).build();
        when(problemRepository.findAllById(problemIds)).thenReturn(problems);
        when(memberService.findOneById(host.getId())).thenReturn(host);

        //when
        Room createdRoom = roomService.createRoom(request, 1L);

        //then
        assertThat(createdRoom).isNotNull();
        assertThat(createdRoom.getName()).isEqualTo("test-room");
        assertThat(createdRoom.getRoomProblems().size()).isEqualTo(2);
        verify(memberService).findOneById(host.getId());
        verify(problemRepository).findAllById(problemIds);
        verify(roomRepository).save(createdRoom);
        verify(roomRedisRepository).save(createdRoom);
    }

    @Test
    @DisplayName("ID로 테스트방을 정상적으로 조회할 수 있다")
    void findRoomById_Found() {
        //given
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        //when
        Room findRoom = roomService.findRoomById(room.getId());

        //then
        assertThat(findRoom).isEqualTo(room);
        verify(roomRepository).findById(room.getId());
    }

    @Test
    @DisplayName("존재하지 않는 ID로 테스트방을 조회 시, 예외를 반환한다")
    void findRoomById_NotFound() {
        //given
        when(roomRepository.findById(any(String.class))).thenReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> roomService.findRoomById("not-exist-id"))
            .isInstanceOf(RuntimeException.class);
        verify(roomRepository).findById(any(String.class));
    }

    @Test
    @DisplayName("테스트방 정보를 성공적으로 업데이트(수정)할 수 있다")
    void updateRoom() {
        //given
        RoomUpdateRequest request = RoomUpdateRequest.builder().maxRoomMembers(5).build();

        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        //when
        RoomUpdateResult result = roomService.updateRoom(room.getId(), request);

        //then
        assertThat(result.getState()).isEqualTo(ROOM_UPDATED);
        assertThat(result.getMessage()).isEqualTo("테스트방 변경이 완료되었습니다.");
        verify(roomRepository).findById(room.getId());
    }

    @Test
    @DisplayName("테스트방에 참가자가 정상적으로 입장할 수 있다")
    void enterRoom_Success() {
        //given
        Member member = Member.builder().id(2L).nickname("new-member").build();

        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        when(memberService.findOneById(member.getId())).thenReturn(member);

        //when
        RoomUpdateResult result = roomService.enterRoom(room.getId(), member.getId());

        //then
        assertThat(result.getState()).isEqualTo(ENTRANT_ENTERED);
        assertThat(result.getMessage()).isEqualTo("[new-member]님이 입장했습니다.");
        verify(roomRepository).findById(room.getId());
        verify(memberService).findOneById(member.getId());
    }

    @Test
    @DisplayName("테스트방의 인원이 가득 찬 경우, 입장할 수 없다")
    void enterRoom_FullRoom() {
        //given
        //3명(최대인원) 추가
        room.addMember(Member.builder().id(2L).build());
        room.addMember(Member.builder().id(3L).build());
        room.addMember(Member.builder().id(4L).build());

        Member member = Member.builder().id(5L).nickname("new-member").build();
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        //when
        RoomUpdateResult result = roomService.enterRoom(room.getId(), member.getId());

        //then
        assertThat(result.getState()).isEqualTo(FULL_ROOM);
        assertThat(result.getMessage()).isEqualTo("정원이 초과되어 입장할 수 없습니다.");
        verify(roomRepository).findById(room.getId());
    }

    @Test
    @DisplayName("참가자가 없는 상태에서 방장이 퇴장할 경우, 방이 삭제된다")
    void exitRoom_DeletesRoomIfHostAndEmpty() {
        //given
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        //when
        RoomUpdateResult result = roomService.exitRoom(room.getId(), host.getId());

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
        Member firstMember = Member.builder().id(2L).nickname("first-member").build();
        Member secondMember = Member.builder().id(3L).nickname("second-member").build();

        room.addMember(firstMember);
        room.addMember(secondMember);

        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        //when
        RoomUpdateResult result = roomService.exitRoom(room.getId(), host.getId());

        //then
        assertThat(room.getRoomMembers().size()).isEqualTo(1);
        assertThat(result.getState()).isEqualTo(HOST_CHANGED);
        assertThat(result.getMessage()).isEqualTo("방장이 [first-member]님으로 변경되었습니다.");
        verify(roomRepository).findById(room.getId());
    }

    @Test
    @DisplayName("참가자가 테스트방에서 정상적으로 퇴장할 수 있다")
    void exitRoom_General() {
        //given
        Member member = Member.builder().id(3L).nickname("exit-member").build();
        room.addMember(member);

        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        //when
        RoomUpdateResult result = roomService.exitRoom(room.getId(), member.getId());

        //then
        assertThat(room.getRoomMembers().size()).isEqualTo(0);
        assertThat(result.getState()).isEqualTo(ENTRANT_EXITED);
        assertThat(result.getMessage()).isEqualTo("[exit-member]님이 퇴장했습니다.");
        verify(roomRepository).findById(room.getId());
    }

    @Test
    @DisplayName("ID로 테스트방을 삭제하면 해당 테스트방이 정상적으로 삭제되며, 삭제된 방에 접근하면 예외가 발생한다")
    void deleteRoom() {
        //given
        doNothing().when(roomRepository).deleteById(room.getId());
        when(roomRepository.findById(room.getId())).thenReturn(Optional.empty());

        //when
        roomService.delete(room.getId());

        //then
        assertThatThrownBy(() -> roomService.findRoomById(room.getId()))
            .isInstanceOf(NoSuchElementException.class);
        verify(roomRepository).deleteById(room.getId());
        verify(roomRedisRepository).deleteById(room.getId());
    }
}