package algo_arena.room.controller;

import static algo_arena.room.enums.RoomEvent.*;

import algo_arena.room.dto.request.RoomCreateRequest;
import algo_arena.room.dto.request.RoomSearchRequest;
import algo_arena.room.dto.response.RoomDetailResponse;
import algo_arena.room.dto.response.RoomEventResponse;
import algo_arena.room.dto.response.RoomListResponse;
import algo_arena.room.entity.Room;
import algo_arena.room.service.RoomFindService;
import algo_arena.room.service.RoomLifeService;
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
        @RequestBody RoomCreateRequest request) {

        String username = userDetails.getUsername();
        Room createdRoom = roomLifeService.createRoom(request, username);

        return ResponseEntity.ok(RoomEventResponse.from(createdRoom.getId(), List.of(CREATE), createdRoom));
    }

    /**
     * 테스트방 상세 조회
     */
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDetailResponse> findRoom(@PathVariable("roomId") String roomId) {

        Room room = roomFindService.findRoomById(roomId);

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
     * 테스트방 삭제 - 자동 삭제
     */
    @DeleteMapping("/{roomId}")
    public ResponseEntity<RoomEventResponse> deleteRoom(@PathVariable("roomId") String roomId) {

        roomLifeService.deleteRoomById(roomId);

        return ResponseEntity.noContent().build();
    }
}