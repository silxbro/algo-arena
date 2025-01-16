package algo_arena.room.service;

import static algo_arena.room.enums.RoomEvent.*;

import algo_arena.member.entity.Member;
import algo_arena.member.service.MemberService;
import algo_arena.room.enums.RoomEvent;
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
    public void enterRoom(String roomId, String memberName) {
        Room room = getRoomFromDB(roomId);
        if (room.isFull()) {
            throw new RuntimeException();
        }
        Member member = memberService.findMemberByName(memberName);
        room.enter(member);
    }

    @Transactional
    public RoomEvent exitRoom(String roomId, String memberName) {
        Room room = getRoomFromDB(roomId);
        if (room.isHost(memberName) && !room.existMembers()) {
            return deleteRoom(roomId);
        }
        if (room.isHost(memberName)) {
            return changeRoomHost(room);
        }
        return memberExitRoom(room, memberName);
    }

    private Room getRoomFromDB(String id) {
        roomRedisRepository.deleteById(id);
        return roomRepository.findById(id).orElseThrow();
    }

    private RoomEvent memberExitRoom(Room room, String memberName) {
        room.exit(memberName);
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