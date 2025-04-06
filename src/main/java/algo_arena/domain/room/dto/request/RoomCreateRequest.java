package algo_arena.domain.room.dto.request;

import algo_arena.domain.member.entity.Member;
import algo_arena.domain.room.entity.Room;
import algo_arena.domain.submission.enums.CodeLanguage;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomCreateRequest {

    @NotBlank(message = "테스트방 이름을 입력해 주세요.")
    @Size(min = 2, max = 20, message = "테스트방 이름은 2~20자 사이여야 합니다.")
    private String name;

    @NotNull(message = "최대 인원을 설정해 주세요.")
    @Size(min = 2, max = 10, message = "테스트방 최대 인원은 2~10명이 가능합니다.")
    private Integer maxRoomMembers;

    @NotEmpty(message = "테스트 문제는 최소 1개 이상 지정해야 합니다.")
    @Size(max = 20, message = "테스트 문제는 최대 20개까지 지정 가능합니다.")
    private List<Long> problemIds;

    @NotBlank(message = "테스트 언어를 선택해 주세요.")
    private String languageName;

    @NotNull(message = "테스트 시간을 분단위로 설정해 주세요.")
    @Min(value = 10, message = "테스트 시간은 최소 10분 이상이어야 합니다.")
    @Max(value = 1200, message = "테스트 시간은 1,200분 이하로만 설정 가능합니다.")
    private Integer timeLimit; //분 단위

    public Room toEntity(Member host) {
        return Room.builder()
            .name(name)
            .maxRoomMembers(maxRoomMembers)
            .host(host)
            .language(CodeLanguage.fromName(languageName))
            .timeLimit(timeLimit)
            .build();
    }
}