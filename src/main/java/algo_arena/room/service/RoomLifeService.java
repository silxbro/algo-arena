package algo_arena.room.service;

import static algo_arena.common.exception.enums.ErrorType.*;
import static algo_arena.room.enums.RoomEvent.UPDATE;

import algo_arena.member.entity.Member;
import algo_arena.member.service.MemberService;
import algo_arena.problem.entity.Problem;
import algo_arena.problem.service.ProblemService;
import algo_arena.room.dto.request.RoomCreateRequest;
import algo_arena.room.dto.request.RoomUpdateRequest;
import algo_arena.room.entity.Room;
import algo_arena.room.exception.RoomException;
import algo_arena.room.repository.RoomRedisRepository;
import algo_arena.room.repository.RoomRepository;
import algo_arena.room.service.result.RoomEventResult;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class RoomLifeService {

    private final RoomRepository roomRepository;
    private final RoomRedisRepository roomRedisRepository;
    private final ProblemService problemService;
    private final MemberService memberService;

    @Transactional
    public Room createRoom(RoomCreateRequest request, String memberName) {
        if (roomRepository.isMemberInAnyRoom(memberName)) {
            throw new RoomException(ALREADY_IN_ROOM);
        }
        Member host = memberService.findMemberByName(memberName);
        List<Problem> problems = findAllProblems(request.getProblemIds());
        return createNewRoom(request, host, problems);
    }

    @Transactional
    public RoomEventResult updateRoom(String id, RoomUpdateRequest request, String memberName) {
        Room room = getRoomFromDB(id);
        if (!room.isHost(memberName)) {
            throw new RoomException(INVALID_ROLE);
        }
        List<Problem> problems = findAllProblems(request.getProblemIds());
        updateExistingRoom(room, request, problems);
        return RoomEventResult.from(UPDATE, room);
    }

    private List<Problem> findAllProblems(List<Long> problemIds) {
        return problemIds.stream()
            .map(problemService::findProblemById)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteRoomById(String id) {
        if (!StringUtils.hasText(id)) {
            throw new RoomException(NULL_VALUE);
        }
        roomRedisRepository.deleteById(id);
        roomRepository.deleteById(id);
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