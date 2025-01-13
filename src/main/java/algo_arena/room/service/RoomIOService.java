package algo_arena.room.service;

import static algo_arena.room.dto.response.RoomEvent.*;

import algo_arena.member.entity.Member;
import algo_arena.member.service.MemberService;
import algo_arena.room.dto.response.RoomEvent;
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
    public RoomEvent enterRoom(String id, Long memberId) {
        Room room = getRoomFromDB(id);
        Member member = memberService.findMemberById(memberId);
        room.enter(member);
        return ENTER;
    }

    @Transactional
    public RoomEvent exitRoom(String id, Long memberId) {
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

    private RoomEvent memberExitRoom(Room room, Long memberId) {
        room.exit(memberId);
        roomRedisRepository.save(room);
        return EXIT;
    }

    private RoomEvent changeRoomHost(Room room) {
        room.changeHost();
        roomRedisRepository.save(room);
        return CHANGE_HOST;
    }

    private RoomEvent deleteRoom(String roomId) {
        roomRepository.deleteById(roomId);
        roomRedisRepository.deleteById(roomId);
        return DELETE;
    }
}