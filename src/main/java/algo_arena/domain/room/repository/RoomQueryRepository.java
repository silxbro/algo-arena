package algo_arena.domain.room.repository;

import algo_arena.domain.room.dto.request.RoomSearchRequest;
import algo_arena.domain.room.entity.Room;
import java.util.List;

public interface RoomQueryRepository {

    List<Room> findRoomsBySearch(RoomSearchRequest request);

}