package algo_arena.room.repository;

import static org.assertj.core.api.Assertions.assertThat;

import algo_arena.config.redis.RedisConfig;
import algo_arena.problem.entity.Problem;
import algo_arena.room.dto.request.RoomSearchRequest;
import algo_arena.room.entity.Room;
import algo_arena.submission.entity.Language;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(RedisConfig.class)
class RoomRepositoryTest {

    @Autowired
    RoomRedisRepository roomRepository;

    Room room1, room2, room3, room4;

    @BeforeEach
    void init() {
        room1 = createRoom("드루와 드루와", 5, Arrays.asList(1L, 2L, 3L), Language.PYTHON, 60);
        room2 = createRoom("코딩왕", 10, Arrays.asList(1L, 2L, 3L, 4L, 5L), Language.PYTHON, 30);
        room3 = createRoom("코딩왕 코딩왕", 15, Arrays.asList(1L, 2L, 3L), Language.C_SHARP, 60);
        room4 = createRoom("CODING TEST", 20, Arrays.asList(1L, 2L), Language.PYTHON, 100);
    }

    @AfterEach
    void clear() {
        roomRepository.clear();
    }

    @Test
    @DisplayName("테스트 방을 성공적으로 생성 및 저장할 수 있다")
    void save() {
        //given
        Room room = createRoom("코딩테스트 타파!!", 5, Arrays.asList(1L), Language.JAVA, 30);

        //when
        Room savedRoom = roomRepository.save(room);

        //then
        assertThat(savedRoom).isEqualTo(room);
    }

    @Test
    @DisplayName("아무 조건 없이 검색할 경우, 모든 테스트 방이 조회된다")
    void findAllBySearch_noCondition() {
        //given
        RoomSearchRequest noCondition = RoomSearchRequest.builder().build();

        // when
        List<Room> findRooms = roomRepository.findAllBySearch(noCondition);

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
        List<Room> findRooms1 = roomRepository.findAllBySearch(roomNameCond1);
        List<Room> findRooms2 = roomRepository.findAllBySearch(roomNameCond2);
        List<Room> findRooms3 = roomRepository.findAllBySearch(roomNameCond3);

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
        RoomSearchRequest problemCond1 = RoomSearchRequest.builder().minProblems(3).maxProblems(5).build();
        RoomSearchRequest problemCond2 = RoomSearchRequest.builder().maxProblems(3).build();
        RoomSearchRequest timeLimitCond1 = RoomSearchRequest.builder().minTimeLimit(60).maxTimeLimit(120).build();
        RoomSearchRequest timeLimitCond2 = RoomSearchRequest.builder().maxTimeLimit(30).build();

        // when
        List<Room> findRooms1 = roomRepository.findAllBySearch(problemCond1);
        List<Room> findRooms2 = roomRepository.findAllBySearch(problemCond2);
        List<Room> findRooms3 = roomRepository.findAllBySearch(timeLimitCond1);
        List<Room> findRooms4 = roomRepository.findAllBySearch(timeLimitCond2);

        // then
        assertThat(findRooms1).contains(room1, room2, room3);
        assertThat(findRooms2).contains(room1, room3, room4);
        assertThat(findRooms3).contains(room1, room3, room4);
        assertThat(findRooms4).containsOnly(room2);
    }

    @Test
    @DisplayName("복합 조건으로 테스트방을 검색할 경우, 해당 조건에 부합하는 테스트 방이 조회된다")
    void findAllBySearch_complexCondition() {
        //given
        RoomSearchRequest complexCond1 = RoomSearchRequest.builder().languageName("Python").minTimeLimit(70).build();
        RoomSearchRequest complexCond2 = RoomSearchRequest.builder().maxRoomMembers(15).roomName("코딩").build();
        RoomSearchRequest complexCond3 = RoomSearchRequest.builder().minProblems(3).maxTimeLimit(60).build();

        // when
        List<Room> findRooms1 = roomRepository.findAllBySearch(complexCond1);
        List<Room> findRooms2 = roomRepository.findAllBySearch(complexCond2);
        List<Room> findRooms3 = roomRepository.findAllBySearch(complexCond3);

        // then
        assertThat(findRooms1).containsOnly(room4);
        assertThat(findRooms2).contains(room2, room3);
        assertThat(findRooms3).contains(room1, room3);
    }

    private Room createRoom(String roomName, int maxRoomMembers, List<Long> problemIds, Language language, int timeLimit) {
        Room room = Room.builder()
            .name(roomName)
            .maxRoomMembers(maxRoomMembers)
            .language(language)
            .timeLimit(timeLimit)
            .build();
        room.initProblems(problemIds.stream().map(problemId -> Problem.builder().id(problemId).build()).toList());
        return roomRepository.save(room);
    }
}