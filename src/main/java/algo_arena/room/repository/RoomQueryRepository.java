package algo_arena.room.repository;

import algo_arena.room.dto.request.RoomSearchRequest;
import algo_arena.room.entity.Room;
import java.util.List;

public interface RoomQueryRepository {

    List<Room> findRoomsBySearch(RoomSearchRequest request);

}