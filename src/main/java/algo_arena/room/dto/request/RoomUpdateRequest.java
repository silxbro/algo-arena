package algo_arena.room.dto.request;

import algo_arena.room.entity.Room;
import algo_arena.submission.enums.CodeLanguage;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomUpdateRequest {

    @Size(min = 2, max = 10, message = "테스트방 최대 인원은 2~10명이 가능합니다.")
    private Integer maxRoomMembers;

    @NotEmpty(message = "테스트 문제는 최소 1개 이상 지정해야 합니다.")
    @Size(max = 20, message = "테스트 문제는 최대 20개까지 지정 가능합니다.")
    private List<Long> problemIds;

    private String languageName;

    @Min(value = 10, message = "테스트 시간은 최소 10분 이상이어야 합니다.")
    @Max(value = 1200, message = "테스트 시간은 1,200분 이하로만 설정 가능합니다.")
    private Integer timeLimit;

    public Room toEntity() {
        return Room.builder()
            .maxRoomMembers(maxRoomMembers)
            .language(CodeLanguage.fromName(languageName))
            .timeLimit(timeLimit)
            .build();
    }
}