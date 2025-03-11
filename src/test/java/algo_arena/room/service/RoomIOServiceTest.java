package algo_arena.room.service;

import static algo_arena.room.enums.RoomEvent.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import algo_arena.member.entity.Member;
import algo_arena.member.repository.MemberRepository;
import algo_arena.member.service.MemberService;
import algo_arena.room.entity.Room;
import algo_arena.room.entity.RoomMember;
import algo_arena.room.repository.RoomRedisRepository;
import algo_arena.room.repository.RoomRepository;
import algo_arena.room.service.result.RoomEventResult;
import algo_arena.submission.enums.CodeLanguage;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class RoomIOServiceTest {

    @Autowired
    RoomIOService roomIOService;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RoomRedisRepository roomRedisRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    private Room room;
    private Member host, member;

    @BeforeEach
    void setUp() {
        host = createMember("host");
        member = createMember("member");
        room = createRoom("test-room", 2, host, CodeLanguage.PYTHON, 60);
    }

    @Test
    @DisplayName("테스트룸의 정원이 최대 정원 미만일 때 입장할 경우, 성공적으로 입장한다")
    void enterRoom_Success() {
        //given

        //when
        roomIOService.enterRoom(room.getId(), member.getName());
        List<Member> members = room.getRoomMembers().stream().map(RoomMember::getMember).toList();

        //then
        assertThat(members).containsExactly(member);
    }

    @Test
    @DisplayName("테스트룸의 정원이 최대 정원일 때 입장할 경우, 입장이 실패하여 예외가 발생한다")
    void enterRoom_Fail() {
        //given
        Member member1 = createMember("member1");
        Member member2 = createMember("member2");

        room.enter(member1);
        room.enter(member2);

        //when

        //then
        assertThatThrownBy(() -> roomIOService.enterRoom(room.getId(), member.getName()))
            .isInstanceOf(RuntimeException.class);

        List<Member> members = room.getRoomMembers().stream().map(RoomMember::getMember).toList();
        assertThat(members).containsExactly(member1, member2);
    }

    @Test
    @DisplayName("호스트가 테스트룸에서 퇴장할 경우, 멤버가 없으면 테스트룸이 삭제되고 DELETE 이벤트를 반환한다")
    void exitRoom_HostAndNoMember_Delete() {
        //given
        String roomId = room.getId();

        //when
        RoomEventResult result = roomIOService.exitRoom(room.getId(), host.getName());

        //then
        assertThat(result.getRoomEvent()).isEqualTo(DELETE);
        assertThat(roomRepository.findById(roomId)).isEqualTo(Optional.empty());
        assertThat(roomRedisRepository.findById(roomId)).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("호스트가 테스트룸에서 퇴장할 경우, 멤버가 존재하면 가장 먼저 입장한 멤버가 방장이 되고 CHANGE_HOST 이벤트를 반환한다")
    void exitRoom_HostAndExistMembers_ChangeHost() {
        //given
        Member member1 = createMember("member1");
        Member member2 = createMember("member2");

        room.enter(member1);
        room.enter(member2);

        //when
        RoomEventResult result = roomIOService.exitRoom(room.getId(), host.getName());

        //then
        assertThat(result.getRoomEvent()).isEqualTo(CHANGE_HOST);
        assertThat(room.getHost()).isEqualTo(member1);
        assertThat(room.getRoomMembers().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("호스트가 아닌 멤버가 테스트룸에서 퇴장 시, EXIT 이벤트를 반환한다")
    void exitRoom() {
        //given
        room.enter(member);

        //when
        RoomEventResult result = roomIOService.exitRoom(room.getId(), member.getName());

        //then
        assertThat(result.getRoomEvent()).isEqualTo(EXIT);
        assertThat(room.isMember(member.getName())).isFalse();
    }

    private Member createMember(String nickname) {
        Member member = Member.builder().name(nickname).build();
        return memberRepository.save(member);
    }

    private Room createRoom(String roomName, int maxRoomMembers, Member host, CodeLanguage language, int timeLimit) {
        Room room = Room.builder()
            .name(roomName)
            .maxRoomMembers(maxRoomMembers)
            .host(host)
            .language(language)
            .timeLimit(timeLimit)
            .build();
        roomRedisRepository.save(room);
        return roomRepository.save(room);
    }
}