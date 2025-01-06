package algo_arena.room.service;

import static algo_arena.room.dto.response.RoomUpdateResult.State.ENTRANT_ENTERED;
import static algo_arena.room.dto.response.RoomUpdateResult.State.ENTRANT_EXITED;
import static algo_arena.room.dto.response.RoomUpdateResult.State.HOST_CHANGED;
import static algo_arena.room.dto.response.RoomUpdateResult.State.ROOM_DELETED;

import algo_arena.member.entity.Member;
import algo_arena.member.service.MemberService;
import algo_arena.room.dto.response.RoomUpdateResult;
import algo_arena.room.entity.Room;
import algo_arena.room.repository.RoomRedisRepository;
import algo_arena.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomIOService {

    private final RoomRepository roomRepository;
    private final RoomRedisRepository roomRedisRepository;
    private final MemberService memberService;

    @Transactional
    public RoomUpdateResult enterRoom(String id, Long memberId) {
        Room room = getRoomFromDB(id);
        Member member = memberService.findMemberById(memberId);
        room.addMember(member);
        return new RoomUpdateResult(ENTRANT_ENTERED, member.getNickname());
    }

    @Transactional
    public RoomUpdateResult exitRoom(String id, Long memberId) {
        Room room = getRoomFromDB(id);
        if (room.isHost(memberId) && !room.existMembers()) {
            return deleteRoom(id);
        }
        if (room.isHost(memberId)) {
            return changeRoomHost(room);
        }
        return memberExitRoom(room, memberId);
    }

    private Room getRoomFromDB(String id) {
        roomRedisRepository.deleteById(id);
        return roomRepository.findById(id).orElseThrow();
    }

    private RoomUpdateResult memberExitRoom(Room room, Long memberId) {
        Member removedMember = room.removeMember(memberId);
        roomRedisRepository.save(room);
        return new RoomUpdateResult(ENTRANT_EXITED, removedMember.getNickname());
    }

    private RoomUpdateResult changeRoomHost(Room room) {
        Member changedHost = room.changeHost();
        roomRedisRepository.save(room);
        return new RoomUpdateResult(HOST_CHANGED, changedHost.getNickname());
    }

    private RoomUpdateResult deleteRoom(String roomId) {
        roomRepository.deleteById(roomId);
        roomRedisRepository.deleteById(roomId);
        return new RoomUpdateResult(ROOM_DELETED);
    }
}
