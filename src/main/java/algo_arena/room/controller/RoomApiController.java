package algo_arena.room.controller;

import algo_arena.room.dto.request.RoomCreateRequest;
import algo_arena.room.dto.request.RoomSearchRequest;
import algo_arena.room.dto.request.RoomUpdateRequest;
import algo_arena.room.dto.response.RoomDetailResponse;
import algo_arena.room.dto.response.RoomListResponse;
import algo_arena.room.dto.response.RoomUpdateResponse;
import algo_arena.room.entity.Room;
import algo_arena.room.dto.response.RoomUpdateResult;
import algo_arena.room.service.RoomFindService;
import algo_arena.room.service.RoomIOService;
import algo_arena.room.service.RoomLifeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomApiController {

    private final RoomLifeService roomLifeService;
    private final RoomFindService roomFindService;
    private final RoomIOService roomIOService;

    /**
     * 테스트방 생성
     */
    @PostMapping
    public ResponseEntity<String> createRoom(@RequestBody RoomCreateRequest request) {
        Room newRoom = roomLifeService.createRoom(request, 1L);
        return ResponseEntity.ok(newRoom.getId());
    }

    /**
     * 테스트방 조회(입장 시)
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoomDetailResponse> findRoom(@PathVariable("id") String id) {
        Room room = roomFindService.findRoomById(id);
        return ResponseEntity.ok(RoomDetailResponse.from(room));
    }

    /**
     * 테스트방 검색(목록 조회)
     */
    @GetMapping
    public ResponseEntity<RoomListResponse> findRoomsBySearch(@RequestBody RoomSearchRequest request) {
        List<Room> rooms = roomFindService.findRoomsBySearch(request);
        return ResponseEntity.ok(RoomListResponse.from(rooms));
    }

    /**
     * 테스트방 정보 수정 - 방장
     */
    @PatchMapping("/{id}")
    public ResponseEntity<RoomUpdateResponse> updateRoom(@PathVariable("id") String id, @RequestBody RoomUpdateRequest request) {
        RoomUpdateResult result = roomLifeService.updateRoom(id, request);
        return ResponseEntity.ok(new RoomUpdateResponse(id, result.getMessage()));
    }

    /**
     * 테스트방 참가자 입장
     */
    @PatchMapping("/{id}/enter/{memberId}")
    public ResponseEntity<RoomUpdateResponse> enterRoom(@PathVariable("id") String id, @PathVariable("memberId") Long memberId) {
        RoomUpdateResult result = roomIOService.enterRoom(id, memberId);
        return ResponseEntity.ok(new RoomUpdateResponse(id, result.getMessage()));
    }

    /**
     * 테스트방 참가자 퇴장
     */
    @PatchMapping("/{id}/exit/{memberId}")
    public ResponseEntity<RoomUpdateResponse> exitRoom(@PathVariable("id") String id, @PathVariable("memberId") Long memberId) {
        RoomUpdateResult result = roomIOService.exitRoom(id, memberId);
        return ResponseEntity.ok(new RoomUpdateResponse(id, result.getMessage()));
    }

    /**
     * 테스트방 삭제 - 자동 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable("id") String id) {
        roomLifeService.deleteRoomById(id);
        return ResponseEntity.noContent().build();
    }
}