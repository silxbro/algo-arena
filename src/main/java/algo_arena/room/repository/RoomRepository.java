package algo_arena.room.repository;

import algo_arena.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, String>, RoomQueryRepository {

}