package algo_arena.room.controller;

import static algo_arena.room.enums.RoomEvent.*;

import algo_arena.room.dto.request.RoomUpdateRequest;
import algo_arena.room.dto.response.RoomEventResponse;
import algo_arena.room.entity.Room;
import algo_arena.room.enums.RoomEvent;
import algo_arena.room.service.RoomIOService;
import algo_arena.room.service.RoomLifeService;
import algo_arena.room.service.result.RoomEventResult;
import algo_arena.utils.jwt.service.JwtTokenUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RoomSocketController {

    private final RoomLifeService roomLifeService;
    private final RoomIOService roomIOService;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * 테스트방 정보 업데이트 - 방장 권한
     */
    @MessageMapping("/rooms/{roomId}/update")
    @SendTo("/sub/rooms/{roomId}")
    public RoomEventResponse updateRoom(@DestinationVariable("roomId") String roomId, @Header("token") String token,
        @RequestBody RoomUpdateRequest request) {

        String memberName = jwtTokenUtil.extractUsername(token);

        RoomEventResult result = roomLifeService.updateRoom(roomId, request, memberName);

        return getRoomEventResponse(result);
    }

    /**
     * 테스트방 참가자 입장
     */
    @MessageMapping("/rooms/{roomId}/enter")
    @SendTo("/sub/rooms/{roomId}")
    public RoomEventResponse enterRoom(@DestinationVariable("roomId") String roomId, @Header("token") String token) {

        String memberName = jwtTokenUtil.extractUsername(token);

        RoomEventResult result = roomIOService.enterRoom(roomId, memberName);

        return getRoomEventResponse(result);
    }

    /**
     * 테스트방 참가자 퇴장
     */
    @MessageMapping("/rooms/{roomId}/exit")
    @SendTo("/sub/rooms/{roomId}")
    public RoomEventResponse exitRoom(@DestinationVariable("roomId") String roomId, @Header("token") String token) {

        String memberName = jwtTokenUtil.extractUsername(token);

        RoomEventResult result = roomIOService.exitRoom(roomId, memberName);

        return getRoomEventResponse(result);
    }

    private RoomEventResponse getRoomEventResponse(RoomEventResult result) {
        RoomEvent resultEvent = result.getRoomEvent();
        Room resultRoom = result.getRoomResult();
        return RoomEventResponse.from(resultRoom.getId(), getRoomEvents(resultEvent), resultRoom);
    }

    private List<RoomEvent> getRoomEvents(RoomEvent resultEvent) {
        List<RoomEvent> events = new ArrayList<>();

        if (resultEvent.isChangeHost()) {
            events.add(EXIT);
        }
        events.add(resultEvent);

        return Collections.unmodifiableList(events);
    }
}