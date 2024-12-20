package algo_arena.submission.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
public class SubmissionSearchCond {

    private Integer problemNumber; //문제 번호
    private String memberNickname; //회원 닉네임
    private String languageName; //풀이 언어 이름
    private String resultDescription; //결과 코드

    @Builder
    public SubmissionSearchCond(Integer problemNumber, String memberNickname, String languageName,
        String resultDescription) {
        this.problemNumber = problemNumber;
        this.memberNickname = memberNickname;
        this.languageName = languageName;
        this.resultDescription = resultDescription;
    }
}