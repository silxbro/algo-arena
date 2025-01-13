package algo_arena.room.repository;

import static org.assertj.core.api.Assertions.assertThat;

import algo_arena.config.redis.RedisConfig;
import algo_arena.problem.entity.Problem;
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
class RoomRedisRepositoryTest {

    @Autowired
    RoomRedisRepository roomRedisRepository;

    Room room;

    @BeforeEach
    void init() {
        room = createRoom("코딩테스트 타파!!", 5, Arrays.asList(1L), Language.JAVA, 30);
        roomRedisRepository.save(room);
    }

    @AfterEach
    void clear() {
        roomRedisRepository.clear();
    }

    @Test
    @DisplayName("테스트 방을 성공적으로 저장 및 ID 조회할 수 있다")
    void findById() {
        //given

        //when
        Room findRoom = roomRedisRepository.findById(room.getId()).orElse(null);

        //then
        assertThat(findRoom).isNotNull();
        assertThat(findRoom).isEqualTo(room);
    }

    @Test
    @DisplayName("테스트 방을 ID로 삭제할 수 있다")
    void deleteById() {
        //given
        String roomId = room.getId();
        roomRedisRepository.deleteById(roomId);

        //when
        Room findRoom = roomRedisRepository.findById(roomId).orElse(null);

        //then
        assertThat(findRoom).isNull();
    }

    private Room createRoom(String roomName, int maxRoomMembers, List<Long> problemIds, Language language, int timeLimit) {
        Room room = Room.builder()
            .name(roomName)
            .maxRoomMembers(maxRoomMembers)
            .language(language)
            .timeLimit(timeLimit)
            .build();
        room.setProblems(problemIds.stream().map(problemId -> Problem.builder().id(problemId).build()).toList());
        return room;
    }
}