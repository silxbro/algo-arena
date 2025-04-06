package algo_arena.domain.room.repository;

import algo_arena.domain.room.entity.Room;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, String>, RoomQueryRepository {

    @EntityGraph(attributePaths = {"host", "roomMembers", "roomProblems"})
    Optional<Room> findById(String id);

    @Query("select count(r) > 0 from Room r left join r.roomMembers rm "
        + "where r.host.name = :memberName or rm.member.name = :memberName")
    boolean isMemberInAnyRoom(@Param("memberName") String memberName);

}