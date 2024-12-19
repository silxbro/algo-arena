package algo_arena.submission.dto.request;

import lombok.Data;

@Data
public class SubmissionSearchCond {

    private Integer problemNumber; //문제 번호
    private String memberNickname; //회원 닉네임
    private String languageName; //풀이 언어 이름
    private String resultDescription; //결과 코드

}