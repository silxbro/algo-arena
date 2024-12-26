package algo_arena.room.service;

import static algo_arena.room.service.RoomUpdateResult.State.*;

import algo_arena.member.service.MemberService;
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
    private final MemberService memberService;

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
    public RoomUpdateResult update(String id, Room updateInfo) {
        Room room = findOneById(id);
        room.update(updateInfo);
        return new RoomUpdateResult(ROOM_UPDATED);
    }

    @Transactional
    public RoomUpdateResult enter(String id, Long memberId) {
        Room room = findOneById(id);
        boolean success = room.addEntrant(memberId);
        if (!success) {
            return new RoomUpdateResult(FULL_ROOM);
        }
        String enteredEntrantNickname = memberService.findOneById(memberId).getNickname();
        return new RoomUpdateResult(ENTRANT_ENTERED, enteredEntrantNickname);
    }

    @Transactional
    public RoomUpdateResult exit(String id, Long memberId) {
        Room room = findOneById(id);
        if (room.isHost(memberId) && room.hasNoEntrants()) {
            delete(id);
            return new RoomUpdateResult(ROOM_DELETED);
        }
        if (room.isHost(memberId)) {
            room.changeHost();
            String changedHostNickname = memberService.findOneById(room.getHostId()).getNickname();
            return new RoomUpdateResult(HOST_CHANGED, changedHostNickname);
        }
        room.removeEntrant(memberId);
        String exitedEntrantNickname = memberService.findOneById(memberId).getNickname();
        return new RoomUpdateResult(ENTRANT_EXITED, exitedEntrantNickname);
    }

    @Transactional
    public void delete(String id) {
        roomRepository.deleteById(id);
    }
}