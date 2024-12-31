package algo_arena.submission.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubmissionSearchRequest {

    private Long problemNumber; //문제 번호
    private String memberNickname; //회원 닉네임
    private String languageName; //풀이 언어 이름
    private String resultDescription; //결과 코드

}