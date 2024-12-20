package algo_arena.member.dto.response;

import algo_arena.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberBasicResponse {

    private String email;
    private String nickname;
    private int problemCount;

    public static MemberBasicResponse from(Member member) {
        return new MemberBasicResponse(member.getEmail(), member.getNickname(), member.getProblemCount());
    }

}