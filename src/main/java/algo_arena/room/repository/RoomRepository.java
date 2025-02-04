package algo_arena.room.repository;

import algo_arena.room.entity.Room;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, String>, RoomQueryRepository {

    @EntityGraph(attributePaths = {"host", "roomMembers", "roomProblems"})
    Optional<Room> findById(String id);

}