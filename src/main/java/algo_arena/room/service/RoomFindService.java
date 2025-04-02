package algo_arena.room.service;

import static algo_arena.common.exception.enums.ErrorType.*;

import algo_arena.room.dto.request.RoomSearchRequest;
import algo_arena.room.entity.Room;
import algo_arena.room.exception.RoomException;
import algo_arena.room.repository.RoomRedisRepository;
import algo_arena.room.repository.RoomRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomFindService {

    private final RoomRepository roomRepository;
    private final RoomRedisRepository roomRedisRepository;


    public Room findRoomById(String id, String username) {
        Room room = getRoom(id);
        if (!room.isMember(username)) {
            throw new RoomException(NOT_IN_ROOM);
        }
        return room;
    }

    public List<Room> findRoomsBySearch(RoomSearchRequest request) {
        validateSearchRequestRange(request);
        return roomRepository.findRoomsBySearch(request);
    }

    private Room getRoom(String id) {
        if (!StringUtils.hasText(id)) {
            throw new RoomException(NULL_VALUE);
        }

        Room redisRoom = roomRedisRepository.findById(id).orElse(null);
        if (redisRoom != null) {
            return redisRoom;
        }

        Room room = roomRepository.findById(id)
            .orElseThrow(() -> new RoomException(ROOM_NOT_FOUND));
        roomRedisRepository.save(room);
        return room;
    }

    private void validateSearchRequestRange(RoomSearchRequest request) {
        Integer minProblems = request.getMinProblems();
        Integer maxProblems = request.getMaxProblems();
        Integer minTimeLimit = request.getMinTimeLimit();
        Integer maxTimeLimit = request.getMaxTimeLimit();
        if (minProblems != null && maxProblems != null && minProblems > maxProblems) {
            throw new RoomException(INVALID_ROOM_PROBLEM_RANGE);
        }
        if (minTimeLimit != null && maxTimeLimit != null && minTimeLimit > maxTimeLimit) {
            throw new RoomException(INVALID_ROOM_TIME_LIMIT_RANGE);
        }
    }
}