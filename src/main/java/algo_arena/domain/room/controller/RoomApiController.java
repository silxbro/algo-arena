package algo_arena.domain.room.controller;

import algo_arena.domain.room.dto.request.RoomSearchRequest;
import algo_arena.domain.room.dto.response.RoomEventResponse;
import algo_arena.domain.room.entity.Room;
import algo_arena.domain.room.enums.RoomEvent;
import algo_arena.domain.room.service.RoomFindService;
import algo_arena.domain.room.service.RoomLifeService;
import algo_arena.domain.room.dto.request.RoomCreateRequest;
import algo_arena.domain.room.dto.response.RoomDetailResponse;
import algo_arena.domain.room.dto.response.RoomListResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    private final RoomLifeService roomLifeService;
    private final RoomFindService roomFindService;

    /**
     * 테스트방 생성
     */
    @PostMapping
    public ResponseEntity<RoomEventResponse> createRoom(@AuthenticationPrincipal UserDetails userDetails,
        @Valid @RequestBody RoomCreateRequest request) {

        String username = userDetails.getUsername();
        Room createdRoom = roomLifeService.createRoom(request, username);

        return ResponseEntity.ok(RoomEventResponse.from(createdRoom.getId(), List.of(
            RoomEvent.CREATE), createdRoom));
    }

    /**
     * 테스트방 상세 조회
     */
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDetailResponse> findRoom(@AuthenticationPrincipal UserDetails userDetails,
        @PathVariable(value = "roomId", required = false) String roomId) {

        String username = userDetails.getUsername();
        Room room = roomFindService.findRoomById(roomId, username);

        return ResponseEntity.ok(RoomDetailResponse.from(room));
    }

    /**
     * 테스트방 검색(목록 조회)
     */
    @GetMapping
    public ResponseEntity<RoomListResponse> findRoomsBySearch(@Valid @RequestBody RoomSearchRequest request) {

        List<Room> rooms = roomFindService.findRoomsBySearch(request);

        return ResponseEntity.ok(RoomListResponse.from(rooms));
    }

    /**
     * 테스트방 삭제 - 자동 삭제
     */
    @DeleteMapping("/{roomId}")
    public ResponseEntity<RoomEventResponse> deleteRoom(@PathVariable(value = "roomId", required = false) String roomId) {

        roomLifeService.deleteRoomById(roomId);

        return ResponseEntity.noContent().build();
    }
}