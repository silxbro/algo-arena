package algo_arena.room.service;

import algo_arena.room.dto.request.RoomSearchRequest;
import algo_arena.room.entity.Room;
import algo_arena.room.repository.RoomRedisRepository;
import algo_arena.room.repository.RoomRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomFindService {

    private final RoomRepository roomRepository;
    private final RoomRedisRepository roomRedisRepository;


    public Room findRoomById(String id) {
        Room redisRoom = roomRedisRepository.findById(id).orElse(null);
        if (redisRoom != null) {
            return redisRoom;
        }
        Room room = roomRepository.findById(id).orElseThrow();
        roomRedisRepository.save(room);
        return room;
    }

    public List<Room> findRoomsBySearch(RoomSearchRequest request) {
        return roomRepository.findRoomsBySearch(request);
    }
}