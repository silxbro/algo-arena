package algo_arena.room.service;

import static algo_arena.common.exception.enums.ErrorType.*;
import static algo_arena.room.enums.RoomEvent.*;

import algo_arena.member.entity.Member;
import algo_arena.member.service.MemberService;
import algo_arena.room.entity.Room;
import algo_arena.room.exception.RoomException;
import algo_arena.room.repository.RoomRedisRepository;
import algo_arena.room.repository.RoomRepository;
import algo_arena.room.service.result.RoomEventResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class RoomIOService {

    private final RoomRepository roomRepository;
    private final RoomRedisRepository roomRedisRepository;
    private final MemberService memberService;

    @Transactional
    public RoomEventResult enterRoom(String roomId, String memberName) {
        Room room = getRoomFromDB(roomId);
        if (room.isFull()) {
            throw new RoomException(ROOM_FULL);
        }
        Member member = memberService.findMemberByName(memberName);
        room.enter(member);
        return RoomEventResult.from(ENTER, room);
    }

    @Transactional
    public RoomEventResult exitRoom(String roomId, String memberName) {
        Room room = getRoomFromDB(roomId);
        if (!room.isMember(memberName)) {
            throw new RoomException(NOT_IN_ROOM);
        }

        if (room.isHost(memberName) && !room.existMembers()) {
            deleteRoom(room.getId());
            return RoomEventResult.from(DELETE);
        }
        if (room.isHost(memberName)) {
            changeRoomHost(room);
            return RoomEventResult.from(CHANGE_HOST, room);
        }
        memberExitRoom(room, memberName);
        return RoomEventResult.from(EXIT, room);
    }

    private Room getRoomFromDB(String id) {
        if (!StringUtils.hasText(id)) {
            throw new RoomException(NULL_VALUE);
        }
        roomRedisRepository.deleteById(id);
        return roomRepository.findById(id)
            .orElseThrow(() -> new RoomException(ROOM_NOT_FOUND));
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
        roomRedisRepository.deleteById(roomId);
        roomRepository.deleteById(roomId);
    }
}