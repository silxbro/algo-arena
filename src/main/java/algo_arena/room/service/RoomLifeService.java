package algo_arena.room.service;

import algo_arena.member.entity.Member;
import algo_arena.member.service.MemberService;
import algo_arena.problem.entity.Problem;
import algo_arena.problem.repository.ProblemRepository;
import algo_arena.room.dto.request.RoomCreateRequest;
import algo_arena.room.dto.request.RoomUpdateRequest;
import algo_arena.room.entity.Room;
import algo_arena.room.repository.RoomRedisRepository;
import algo_arena.room.repository.RoomRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomLifeService {

    private final RoomRepository roomRepository;
    private final RoomRedisRepository roomRedisRepository;
    private final ProblemRepository problemRepository;
    private final MemberService memberService;

    @Transactional
    public Room createRoom(RoomCreateRequest request, Long hostId) {
        Member host = memberService.findMemberById(hostId);
        List<Problem> problems = problemRepository.findAllById(request.getProblemIds());
        return createNewRoom(request, host, problems);
    }

    @Transactional
    public void updateRoom(String id, RoomUpdateRequest request) {
        Room room = getRoomFromDB(id);
        List<Problem> problems = problemRepository.findAllById(request.getProblemIds());
        updateExistingRoom(room, request, problems);
    }

    @Transactional
    public void deleteRoomById(String id) {
        roomRepository.deleteById(id);
        roomRedisRepository.deleteById(id);
    }

    private Room getRoomFromDB(String id) {
        roomRedisRepository.deleteById(id);
        return roomRepository.findById(id).orElseThrow();
    }

    private Room createNewRoom(RoomCreateRequest request, Member host, List<Problem> problems) {
        Room newRoom = request.toEntity(host);
        newRoom.setProblems(problems);

        roomRedisRepository.save(newRoom);
        return roomRepository.save(newRoom);
    }

    private void updateExistingRoom(Room room, RoomUpdateRequest request, List<Problem> problems) {
        room.update(request.toEntity());
        room.setProblems(problems);

        roomRedisRepository.save(room);
    }
}