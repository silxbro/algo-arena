package algo_arena.room.service;

import static algo_arena.room.enums.RoomEvent.*;

import algo_arena.member.entity.Member;
import algo_arena.member.service.MemberService;
import algo_arena.room.entity.Room;
import algo_arena.room.repository.RoomRedisRepository;
import algo_arena.room.repository.RoomRepository;
import algo_arena.room.service.result.RoomEventResult;
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
    public RoomEventResult enterRoom(String roomId, String memberName) {
        Room room = getRoomFromDB(roomId);
        if (room.isMember(memberName)) {
            throw new RuntimeException();
        }
        if (room.isFull()) {
            throw new RuntimeException();
        }
        Member member = memberService.findMemberByName(memberName);
        room.enter(member);
        return RoomEventResult.from(ENTER, room);
    }

    @Transactional
    public RoomEventResult exitRoom(String roomId, String memberName) {
        Room room = getRoomFromDB(roomId);
        if (!room.isMember(memberName)) {
            throw new RuntimeException();
        }
        if (room.isHost(memberName) && !room.existMembers()) {
            deleteRoom(roomId);
            return RoomEventResult.from(DELETE);
        }
        if (room.isHost(memberName)) {
            changeRoomHost(room);
            return RoomEventResult.from(CHANGE_HOST, room);
        }
        memberExitRoom(room, memberName);
        return RoomEventResult.from(EXIT, room);
    }

    public void checkAlreadyEntrance(String memberName) {
        boolean isAlreadyEntered = roomRepository.findAll().stream()
            .anyMatch(room -> room.isMember(memberName));
        if (isAlreadyEntered) {
            throw new RuntimeException();
        }
    }

    private Room getRoomFromDB(String id) {
        roomRedisRepository.deleteById(id);
        return roomRepository.findById(id).orElseThrow();
    }

    private void memberExitRoom(Room room, String memberName) {
        room.exit(memberName);
        roomRedisRepository.save(room);
    }

    private void changeRoomHost(Room room) {
        room.changeHost();
        roomRedisRepository.save(room);
    }

    private void deleteRoom(String roomId) {
        roomRepository.deleteById(roomId);
        roomRedisRepository.deleteById(roomId);
    }
}