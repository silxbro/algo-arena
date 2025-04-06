package algo_arena.domain.submission.enums;

import java.util.Arrays;

public enum SubmissionResult {

    CORRECT("정답"),
    WRONG("오답"),
    TIME_EXCEEDED("시간 초과"),
    MEMORY_EXCEEDED("메모리 초과"),
    OUTPUT_EXCEEDED("출력 초과"),
    COMPILE_ERROR("컴파일 오류"),
    RUNTIME_ERROR("런타임 오류"),
    FORMAT_ERROR("출력 형식 오류"),
    ;

    private final String description;

    SubmissionResult(String description) {
        this.description = description;
    }

    public static SubmissionResult fromDescription(String description) {
        return Arrays.stream(values())
            .filter(result -> result.description.equals(description))
            .findAny().orElse(null);
    }

    public String getDescription() {
        return description;
    }
}
