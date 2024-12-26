package algo_arena.room.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import algo_arena.room.entity.Room;
import algo_arena.room.repository.RoomRedisRepository;
import algo_arena.submission.entity.Language;
import java.util.List;
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
class RoomServiceTest {

    @Mock
    RoomRedisRepository roomRedisRepository;

    @InjectMocks
    RoomService roomService;

    Room room;

    @BeforeEach
    void setUp() {
        room = Room.builder()
            .name("Test Room")
            .maxEntrants(10)
            .language(Language.JAVA_SCRIPT)
            .problemIds(List.of(1L, 2L, 3L))
            .hostId(10L)
            .timeLimit(60)
            .build();
    }

    @Test
    @DisplayName("새로운 테스트방을 성공적으로 생성할 수 있다")
    void createRoom() {
        //given
        when(roomRedisRepository.save(any(Room.class))).thenReturn(room);

        //when
        Room createdRoom = roomService.create(room);

        //then
        assertThat(createdRoom).isNotNull();
        assertThat(createdRoom).isEqualTo(room);
        verify(roomRedisRepository, times(1)).save(room);
    }

    @Test
    @DisplayName("ID로 테스트방을 조회하면 해당 테스트방을 정상적으로 반환한다")
    void findOneById() {
        //given
        when(roomRedisRepository.findById(room.getId())).thenReturn(Optional.of(room));

        //when
        Room findRoom = roomService.findOneById(room.getId());

        //then
        assertThat(findRoom).isNotNull();
        assertThat(findRoom).isEqualTo(room);
        verify(roomRedisRepository, times(1)).findById(room.getId());
    }

    @Test
    @DisplayName("ID로 테스트방을 삭제하면 해당 테스트방이 정상적으로 삭제되며, 삭제된 방에 접근하면 예외가 발생한다")
    void deleteRoom() {
        // Given
        doNothing().when(roomRedisRepository).deleteById(room.getId());

        // When
        roomService.delete(room.getId());

        // Then
        assertThatThrownBy(() -> roomService.findOneById(room.getId()))
            .isInstanceOf(NoSuchElementException.class);
        verify(roomRedisRepository, times(1)).deleteById(room.getId());
    }
}