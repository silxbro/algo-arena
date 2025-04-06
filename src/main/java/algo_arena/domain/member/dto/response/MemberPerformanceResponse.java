package algo_arena.domain.member.dto.response;

import algo_arena.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberPerformanceResponse {

    private String name;
    private long testCount; //참가 테스트 개수
    private long submissionCount; // 제출 문제 개수
    private long rightCount; //정답 문제 개수
    private long wrongCount; //오답 문제 개수
    private long timeExceedCount; //시간 초과 문제 개수
    private long memoryExceedCount; //메모리 초과 문제 개수
    private long compileErrorCount; //컴파일 오류 문제 개수
    private long runtimeErrorCount; //런타임 오류 문제 개수
    private long outputExceedCount; //출력 초과 문제 개수
    private long formatErrorCount; //출력 형식 오류 개수

    public static MemberPerformanceResponse from(Member member) {
        return MemberPerformanceResponse.builder()
            .name(member.getName())
            .testCount(member.getTestCount())
            .submissionCount(member.getSubmissionCount())
            .rightCount(member.getRightCount())
            .wrongCount(member.getWrongCount())
            .timeExceedCount(member.getTimeExceedCount())
            .memoryExceedCount(member.getMemoryExceedCount())
            .compileErrorCount(member.getCompileErrorCount())
            .runtimeErrorCount(member.getRuntimeErrorCount())
            .outputExceedCount(member.getOutputExceedCount())
            .formatErrorCount(member.getFormatErrorCount())
            .build();
    }
}