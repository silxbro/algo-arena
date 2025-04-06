package algo_arena.domain.room.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomSearchRequest {

    private String roomName;

    @Size(min = 2, max = 10, message = "테스트방 최대 인원은 2~10명입니다.")
    private Integer maxRoomMembers;

    private String languageName;

    @Positive(message = "테스트 문제 개수는 자연수입니다.")
    @Max(value = 20, message = "테스트 문제 개수는 최대 20개입니다.")
    private Integer minProblems;

    @Positive(message = "테스트 문제 개수는 자연수입니다.")
    @Max(value = 20, message = "테스트 문제 개수는 최대 20개입니다.")
    private Integer maxProblems;

    @Min(value = 10, message = "테스트 시간은 최소 10분입니다.")
    @Max(value = 1200, message = "테스트 시간은 최대 1,200분입니다.")
    private Integer minTimeLimit;

    @Min(value = 10, message = "테스트 시간은 최소 10분입니다.")
    @Max(value = 1200, message = "테스트 시간은 최대 1,200분입니다.")
    private Integer maxTimeLimit;

}