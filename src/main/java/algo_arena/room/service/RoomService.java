package algo_arena.room.service;

import static algo_arena.room.service.RoomUpdateResult.State.*;

import algo_arena.member.entity.Member;
import algo_arena.member.service.MemberService;
import algo_arena.problem.entity.Problem;
import algo_arena.problem.repository.ProblemRepository;
import algo_arena.room.dto.request.RoomCreateRequest;
import algo_arena.room.dto.request.RoomSearchRequest;
import algo_arena.room.dto.request.RoomUpdateRequest;
import algo_arena.room.entity.Room;
import algo_arena.room.repository.RoomRedisRepository;
import algo_arena.room.repository.RoomRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomRedisRepository roomRedisRepository;
    private final ProblemRepository problemRepository;
    private final MemberService memberService;

    @Transactional
    public Room createRoom(RoomCreateRequest request, Long hostId) {
        Member host = memberService.findOneById(hostId);
        List<Problem> problems = problemRepository.findAllById(request.getProblemIds());
        return createNewRoom(request, host, problems);
    }

    public Room findRoomById(String id) {
        Room redisRoom = roomRedisRepository.findById(id).orElse(null);
        if (redisRoom != null) {
            return redisRoom;
        }
        Room room = roomRepository.findById(id).orElseThrow();
        roomRedisRepository.save(room);
        return room;
    }

    public List<Room> findRooms(RoomSearchRequest request) {
        return roomRepository.findRoomsBySearch(request);
    }

    @Transactional
    public RoomUpdateResult updateRoom(String id, RoomUpdateRequest request) {
        roomRedisRepository.deleteById(id);
        Room room = roomRepository.findById(id).orElseThrow();
        List<Problem> problems = problemRepository.findAllById(request.getProblemIds());
        updateExistingRoom(room, request, problems);

        roomRedisRepository.save(room);

        return new RoomUpdateResult(ROOM_UPDATED);
    }

    @Transactional
    public RoomUpdateResult enterRoom(String id, Long memberId) {
        roomRedisRepository.deleteById(id);
        Room room = roomRepository.findById(id).orElseThrow();
        Member member = memberService.findOneById(memberId);
        room.addMember(member);
        return new RoomUpdateResult(ENTRANT_ENTERED, member.getNickname());
    }

    @Transactional
    public RoomUpdateResult exitRoom(String id, Long memberId) {
        roomRedisRepository.deleteById(id);
        Room room = roomRepository.findById(id).orElseThrow();
        if (room.isHost(memberId) && !room.existMembers()) {
            delete(id);
            return new RoomUpdateResult(ROOM_DELETED);
        }
        if (room.isHost(memberId)) {
            Member changedHost = room.changeHost();
            roomRedisRepository.save(room);
            return new RoomUpdateResult(HOST_CHANGED, changedHost.getNickname());
        }
        Member removedMember = room.removeMember(memberId);
        roomRedisRepository.save(room);
        return new RoomUpdateResult(ENTRANT_EXITED, removedMember.getNickname());
    }

    @Transactional
    public void delete(String id) {
        roomRepository.deleteById(id);
        roomRedisRepository.deleteById(id);
    }

    private Room createNewRoom(RoomCreateRequest request, Member host, List<Problem> problems) {
        Room newRoom = request.toEntity(host);
        newRoom.setProblems(problems);

        roomRepository.save(newRoom);
        roomRedisRepository.save(newRoom);
        return newRoom;
    }

    private void updateExistingRoom(Room room, RoomUpdateRequest request, List<Problem> problems) {
        room.update(request.toEntity());
        room.setProblems(problems);
    }
}