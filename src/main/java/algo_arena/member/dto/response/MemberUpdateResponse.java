package algo_arena.member.dto.response;

import algo_arena.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberUpdateResponse {

    private final static String DEFAULT_MESSAGE = "회원정보가 변경되었습니다.";
    private String message;
    private MemberDetailResponse memberDetailResponse;

    public static MemberUpdateResponse from(Member member) {
        return new MemberUpdateResponse(DEFAULT_MESSAGE, MemberDetailResponse.from(member));
    }
    public static MemberUpdateResponse from(String message, Member member) {
        return new MemberUpdateResponse(message, MemberDetailResponse.from(member));
    }
}