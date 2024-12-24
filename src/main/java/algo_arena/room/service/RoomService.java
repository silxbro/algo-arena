package algo_arena.room.service;

import algo_arena.room.dto.request.RoomSearchCond;
import algo_arena.room.entity.Room;
import algo_arena.room.repository.RoomRedisRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {

    private final RoomRedisRepository roomRepository;

    @Transactional
    public Room create(Room room) {
        return roomRepository.save(room);
    }

    public Room findOneById(String id) {
        return roomRepository.findById(id).orElseThrow();
    }

    public List<Room> findAll(RoomSearchCond searchCond) {
        return roomRepository.findAllBySearch(searchCond);
    }

    @Transactional
    public void delete(String id) {
        roomRepository.deleteById(id);
    }
}