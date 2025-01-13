package algo_arena.room.service;

import static algo_arena.room.dto.response.RoomEvent.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import algo_arena.member.entity.Member;
import algo_arena.member.service.MemberService;
import algo_arena.room.dto.response.RoomEvent;
import algo_arena.room.entity.Room;
import algo_arena.room.repository.RoomRedisRepository;
import algo_arena.room.repository.RoomRepository;
import algo_arena.submission.entity.Language;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoomIOServiceTest {

    @InjectMocks
    RoomIOService roomIOService;

    @Mock
    RoomRepository roomRepository;

    @Mock
    RoomRedisRepository roomRedisRepository;

    @Mock
    MemberService memberService;

    private Room room;
    private Member host, member;

    @BeforeEach
    void setUp() {
        host = createMember(1L, "host-member");
        member = createMember(2L, "room-member");
        room = createRoom("test-room", 5, host, Language.PYTHON, 60);
    }

    @Test
    @DisplayName("테스트룸에 입장 시, 해당 회원을 목록에 추가하고 ENTER 이벤트를 반환한다")
    void enterRoom() {
        //given
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        when(memberService.findMemberById(member.getId())).thenReturn(member);

        //when
        RoomEvent roomEvent = roomIOService.enterRoom(room.getId(), member.getId());

        //then
        assertThat(roomEvent).isEqualTo(ENTER);
        verify(roomRedisRepository).deleteById(room.getId());
        verify(roomRepository).findById(room.getId());
        verify(memberService).findMemberById(member.getId());
    }

    @Test
    @DisplayName("호스트가 테스트룸에서 퇴장할 경우, 멤버가 없으면 테스트룸이 삭제되고 DELETE 이벤트를 반환한다")
    void exitRoom_HostAndNoMembers_delete() {
        //given
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        //when
        RoomEvent roomEvent = roomIOService.exitRoom(room.getId(), host.getId());

        //then
        assertThat(roomEvent).isEqualTo(DELETE);
        verify(roomRepository).findById(room.getId());
        verify(roomRedisRepository, times(2)).deleteById(room.getId());
        verify(roomRepository).deleteById(room.getId());
    }

    @Test
    @DisplayName("호스트가 테스트룸에서 퇴장할 경우, 멤버가 있으면 가장 먼저 입장한 멤버가 방장이 되고 CHANGE_HOST 이벤트를 반환한다")
    void exitRoom_HostAndExistMembers_changeHost() {
        //given
        Member member1 = createMember(3L, "member1");
        Member member2 = createMember(4L, "member2");
        room.enter(member1);
        room.enter(member2);

        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        //when
        RoomEvent roomEvent = roomIOService.exitRoom(room.getId(), host.getId());

        //then
        assertThat(roomEvent).isEqualTo(CHANGE_HOST);
        assertThat(room.getHost()).isEqualTo(member1);
        assertThat(room.getRoomMembers().size()).isEqualTo(1);
        verify(roomRedisRepository).deleteById(room.getId());
        verify(roomRepository).findById(room.getId());
        verify(roomRedisRepository).save(room);
    }

    @Test
    @DisplayName("테스트룸에서 퇴장 시, 해당 회원을 목록에서 삭제하고 EXIT 이벤트를 반환한다")
    void exitRoom() {
        //given
        Member member1 = createMember(3L, "member1");
        Member member2 = createMember(4L, "member2");
        room.enter(member1);
        room.enter(member2);

        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        //when
        RoomEvent roomEvent = roomIOService.exitRoom(room.getId(), member2.getId());

        //then
        assertThat(roomEvent).isEqualTo(EXIT);
        assertThat(room.isMember(3L)).isTrue();
        assertThat(room.isMember(4L)).isFalse();
        verify(roomRedisRepository).deleteById(room.getId());
        verify(roomRepository).findById(room.getId());
        verify(roomRedisRepository).save(room);
    }

    private Member createMember(Long id, String nickname) {
        return Member.builder().id(id).nickname(nickname).build();
    }

    private Room createRoom(String roomName, int maxRoomMembers, Member host, Language language, int timeLimit) {
        return Room.builder()
            .name(roomName)
            .maxRoomMembers(maxRoomMembers)
            .host(host)
            .language(language)
            .timeLimit(timeLimit)
            .build();
    }

}