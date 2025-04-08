package algo_arena.chat.factory;

import static algo_arena.domain.chat.enums.MessageType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import algo_arena.domain.chat.factory.SystemMessageFactory;
import algo_arena.domain.member.entity.Member;
import algo_arena.domain.room.entity.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SystemMessageFactoryTest {

    @Autowired
    SystemMessageFactory systemMessageFactory;

    Room room;

    String memberName;

    @BeforeEach
    void setUp() {
        room = Mockito.mock(Room.class);
        memberName = "Alice";
    }

    @Test
    void create_EnterMessage() {
        //given

        //when
        String result = systemMessageFactory.create(ENTER, room, memberName);

        //then
        assertThat(result).isEqualTo("[알림] Alice 님이 입장했습니다.");

    }

    @Test
    void create_ExitMessage() {
        //given

        //when
        String result = systemMessageFactory.create(EXIT, room, memberName);

        //then
        assertThat(result).isEqualTo("[알림] Alice 님이 퇴장했습니다.");
    }

    @Test
    void create_CreateRoom() {
        //given
        Mockito.when(room.getName()).thenReturn("TestRoom");

        //when
        String result = systemMessageFactory.create(CREATE, room, memberName);

        //then
        assertThat(result).isEqualTo("[알림] Alice 님이 <TestRoom> 방을 생성했습니다.");
    }

    @Test
    void create_ChangeHost() {
        //given
        Member newHost = Mockito.mock(Member.class);
        Mockito.when(room.getHost()).thenReturn(newHost);
        Mockito.when(newHost.getName()).thenReturn("John");

        //when
        String result = systemMessageFactory.create(CHANGE_HOST, room, null);

        //then
        assertThat(result).isEqualTo("[알림] 방장이 John 님으로 변경되었습니다.");
    }

    @Test
    void create_InvalidMessageType() {
        //given

        //when

        //then
        assertThrows(NullPointerException.class, () ->
            systemMessageFactory.create(null, room, memberName)
        );
    }
}