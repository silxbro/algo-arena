package algo_arena.room.controller;

import algo_arena.member.entity.Member;
import algo_arena.member.repository.MemberRepository;
import algo_arena.problem.entity.Problem;
import algo_arena.problem.repository.ProblemRepository;
import algo_arena.room.dto.request.RoomCreateRequest;
import algo_arena.room.dto.request.RoomSearchCond;
import algo_arena.room.dto.response.RoomDetailResponse;
import algo_arena.room.dto.response.RoomDetailResponse.RoomHost;
import algo_arena.room.dto.response.RoomDetailResponse.RoomParticipant;
import algo_arena.room.dto.response.RoomListResponse;
import algo_arena.room.entity.Participant;
import algo_arena.room.entity.Room;
import algo_arena.room.service.RoomService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomApiController {

    private final RoomService roomService;
    private final MemberRepository memberRepository;
    private final ProblemRepository problemRepository;

    /**
     * 테스트방 생성
     */
    @PostMapping
    public ResponseEntity<String> createRoom(@RequestBody RoomCreateRequest request) {
        Room newRoom = roomService.create(request.toEntity());
        return ResponseEntity.ok(newRoom.getId());
    }

    /**
     * 테스트방 조회(입장 시)
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoomDetailResponse> findRoom(@PathVariable("id") String id) {
        Room room = roomService.findOneById(id);
        List<String> problemTitles = problemRepository.findAllById(room.getProblemIds()).stream().map(Problem::getTitle).toList();
        RoomHost host = convertToRoomHost(room.getHostId());
        List<RoomParticipant> participants = room.getParticipants().stream().map(this::convertToRoomParticipant).toList();
        return ResponseEntity.ok(RoomDetailResponse.from(room, problemTitles, host, participants));
    }

    /**
     * 테스트방 검색(목록 조회)
     */
    @GetMapping
    public ResponseEntity<RoomListResponse> findRoomsBySearch(@RequestBody RoomSearchCond searchCond) {
        List<Room> rooms = roomService.findAll(searchCond);
        return ResponseEntity.ok(RoomListResponse.from(rooms));
    }

    /**
     * 테스트방 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable("id") String id) {
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private RoomParticipant convertToRoomParticipant(Participant participant) {
        Member findParticipant = memberRepository.findById(participant.getMemberId()).orElseThrow();
        return RoomParticipant.builder()
            .nickname(findParticipant.getNickname())
            .imgUrl(findParticipant.getImgUrl())
            .isReady(participant.getIsReady())
            .build();
    }

    private RoomHost convertToRoomHost(Long hostId) {
        Member host = memberRepository.findById(hostId).orElseThrow();
        return RoomHost.builder()
            .nickname(host.getNickname())
            .imgUrl(host.getImgUrl())
            .build();
    }
}