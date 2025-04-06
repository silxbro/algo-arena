package algo_arena.domain.room.dto.response;

import algo_arena.domain.room.entity.Room;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomListResponse {
    private long number;
    private List<RoomBasicResponse> roomList;

    public static RoomListResponse from(Collection<Room> rooms) {
        return RoomListResponse.builder()
            .number(rooms.size())
            .roomList(rooms.stream().map(RoomBasicResponse::from).collect(Collectors.toList()))
            .build();
    }
}