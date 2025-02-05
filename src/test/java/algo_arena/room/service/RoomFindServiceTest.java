package algo_arena.room.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import algo_arena.room.entity.Room;
import algo_arena.room.repository.RoomRedisRepository;
import algo_arena.room.repository.RoomRepository;
import algo_arena.submission.enums.Language;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoomFindServiceTest {

    @InjectMocks
    RoomFindService roomFindService;

    @Mock
    RoomRepository roomRepository;

    @Mock
    RoomRedisRepository roomRedisRepository;

    Room room;

    @BeforeEach
    void setUp() {
        room = createRoom("test-room", 5, Language.PYTHON, 60);
    }

    @Test
    @DisplayName("ID로 테스트룸을 조회할 때, Redis 캐시에 존재하는 경우 Redis 에 저장된 값을 반환한다")
    void findRoomById_InRedis() {
        //given
        when(roomRedisRepository.findById(room.getId())).thenReturn(Optional.of(room));

        //when
        Room findRoom = roomFindService.findRoomById(room.getId());

        //then
        assertThat(findRoom).isNotNull();
        assertThat(findRoom).isEqualTo(room);
        verify(roomRedisRepository).findById(room.getId());
        verify(roomRepository, times(0)).findById(room.getId());
    }

    @Test
    @DisplayName("ID로 테스트룸을 조회할 때, Redis 캐시에 존재하지 않는 경우 데이터베이스에서 값을 조회하고 Redis 캐시에 저장한다")
    void findRoomById_NotInRedis() {
        //given
        when(roomRedisRepository.findById(room.getId())).thenReturn(Optional.empty());
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        //when
        Room findRoom = roomFindService.findRoomById(room.getId());

        //then
        assertThat(findRoom).isNotNull();
        assertThat(findRoom).isEqualTo(room);
        verify(roomRedisRepository).findById(room.getId());
        verify(roomRepository).findById(room.getId());
        verify(roomRedisRepository).save(room);
    }

    @Test
    @DisplayName("ID로 테스트룸을 조회했을 때, Redis 캐시 및 데이터베이스 모두 해당 데이터가 없으면 예외를 발생시킨다")
    void findRoomById_NotFound() {
        //given
        when(roomRedisRepository.findById(room.getId())).thenReturn(Optional.empty());
        when(roomRepository.findById(room.getId())).thenReturn(Optional.empty());

        //when

        //then
        assertThatThrownBy(() -> roomFindService.findRoomById(room.getId()))
            .isInstanceOf(NoSuchElementException.class);
    }

    private Room createRoom(String roomName, int maxRoomMembers, Language language, int timeLimit) {
        return Room.builder()
            .name(roomName)
            .maxRoomMembers(maxRoomMembers)
            .language(language)
            .timeLimit(timeLimit)
            .build();
    }
}