package algo_arena.member.dto.response;

import algo_arena.member.entity.Member;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberListResponse {

    private long number;
    private List<MemberBasicResponse> memberList;

    public static MemberListResponse from(Collection<Member> members) {
        return new MemberListResponse(
            members.size(),
            members.stream().map(MemberBasicResponse::from).collect(Collectors.toList())
        );
    }

}