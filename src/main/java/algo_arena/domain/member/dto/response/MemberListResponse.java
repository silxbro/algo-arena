package algo_arena.domain.member.dto.response;

import algo_arena.domain.member.entity.Member;
import java.util.Collection;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberListResponse {

    private long number;
    private List<MemberBasicResponse> memberList;

    public static MemberListResponse from(Collection<Member> members) {
        return MemberListResponse.builder()
            .number(members.size())
            .memberList(members.stream().map(MemberBasicResponse::from).toList())
            .build();
    }

}