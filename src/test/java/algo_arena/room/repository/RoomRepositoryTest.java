package algo_arena.room.repository;

import static org.assertj.core.api.Assertions.assertThat;

import algo_arena.problem.entity.Problem;
import algo_arena.problem.repository.ProblemRepository;
import algo_arena.room.dto.request.RoomSearchRequest;
import algo_arena.room.entity.Room;
import algo_arena.submission.entity.Language;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoomRepositoryTest {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    ProblemRepository problemRepository;

    Room room1, room2, room3, room4;

    @BeforeAll
    void setUp() {
        problemRepository.saveAll(List.of(
            Problem.builder().build(),
            Problem.builder().build(),
            Problem.builder().build(),
            Problem.builder().build(),
            Problem.builder().build())
        );

        room1 = createRoom("드루와 드루와", 5, Arrays.asList(1L, 2L, 3L), Language.PYTHON, 60);
        room2 = createRoom("코딩왕", 10, Arrays.asList(1L, 2L, 3L, 4L, 5L), Language.PYTHON, 30);
        room3 = createRoom("코딩왕 코딩왕", 15, Arrays.asList(1L, 2L, 3L), Language.C_SHARP, 60);
        room4 = createRoom("CODING TEST", 20, Arrays.asList(1L, 2L), Language.PYTHON, 100);
    }

    @Test
    @DisplayName("아무 조건 없이 검색할 경우, 모든 테스트 방이 조회된다")
    void findAllBySearch_noCondition() {
        //given
        RoomSearchRequest noCondition = RoomSearchRequest.builder().build();

        // when
        List<Room> findRooms = roomRepository.findRoomsBySearch(noCondition);

        // then
        assertThat(findRooms.size()).isEqualTo(4);
        assertThat(findRooms).contains(room1, room2, room3, room4);
    }

    @Test
    @DisplayName("방 이름으로 검색할 경우, 대소문자를 구분하지 않으며 해당 검색어를 포함하는 테스트 방이 조회된다")
    void findAllBySearch_roomNameCondition() {
        //given
        RoomSearchRequest roomNameCond1 = RoomSearchRequest.builder().roomName("test").build();
        RoomSearchRequest roomNameCond2 = RoomSearchRequest.builder().roomName("코딩왕").build();
        RoomSearchRequest roomNameCond3 = RoomSearchRequest.builder().roomName("드루와!").build();

        // when
        List<Room> findRooms1 = roomRepository.findRoomsBySearch(roomNameCond1);
        List<Room> findRooms2 = roomRepository.findRoomsBySearch(roomNameCond2);
        List<Room> findRooms3 = roomRepository.findRoomsBySearch(roomNameCond3);

        // then
        assertThat(findRooms1).containsOnly(room4);
        assertThat(findRooms2.size()).isEqualTo(2);
        assertThat(findRooms2).contains(room2, room3);
        assertThat(findRooms3).isEmpty();
    }

    @Test
    @DisplayName("문제 수 또는 제한 시간으로 범위 검색할 경우, 해당 조건에 부합하는 테스트 방이 조회된다")
    void findAllBySearch_rangeCondition() {
        //given
        RoomSearchRequest problemCond = RoomSearchRequest.builder().minProblems(3).maxProblems(5).build();
        RoomSearchRequest timeLimitCond = RoomSearchRequest.builder().minTimeLimit(60).maxTimeLimit(120).build();

        // when
        List<Room> findRooms1 = roomRepository.findRoomsBySearch(problemCond);
        List<Room> findRooms2 = roomRepository.findRoomsBySearch(timeLimitCond);

        // then
        assertThat(findRooms1.size()).isEqualTo(3);
        assertThat(findRooms2.size()).isEqualTo(3);
        assertThat(findRooms1).contains(room1, room2, room3);
        assertThat(findRooms2).contains(room1, room3, room4);
    }

    @Test
    @DisplayName("복합 조건으로 테스트방을 검색할 경우, 해당 조건에 부합하는 테스트 방이 조회된다")
    void findAllBySearch_complexCondition() {
        //given
        RoomSearchRequest complexCond1 = RoomSearchRequest.builder().languageName("Python").minTimeLimit(70).build();
        RoomSearchRequest complexCond2 = RoomSearchRequest.builder().maxRoomMembers(15).roomName("코딩").build();

        // when
        List<Room> findRooms1 = roomRepository.findRoomsBySearch(complexCond1);
        List<Room> findRooms2 = roomRepository.findRoomsBySearch(complexCond2);

        // then
        assertThat(findRooms1.size()).isEqualTo(1);
        assertThat(findRooms2.size()).isEqualTo(2);
        assertThat(findRooms1).containsOnly(room4);
        assertThat(findRooms2).contains(room2, room3);
    }

    private Room createRoom(String roomName, int maxRoomMembers, List<Long> problemIds, Language language, int timeLimit) {
        Room room = Room.builder()
            .name(roomName)
            .maxRoomMembers(maxRoomMembers)
            .language(language)
            .timeLimit(timeLimit)
            .build();
        room.setProblems(problemRepository.findAllById(problemIds));
        return roomRepository.save(room);
    }
}