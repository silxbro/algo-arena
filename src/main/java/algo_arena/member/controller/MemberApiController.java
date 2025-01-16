package algo_arena.member.controller;

import algo_arena.member.dto.response.MemberListResponse;
import algo_arena.member.dto.response.MemberPerformanceResponse;
import algo_arena.member.dto.response.MemberProfileResponse;
import algo_arena.member.entity.Member;
import algo_arena.member.service.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 회원 정보 조회 (검색)
     */
    @GetMapping
    public ResponseEntity<MemberListResponse> findMembersByNickname(@RequestParam("nickname") String nickname) {

        List<Member> members = memberService.findMembersByNickname(nickname);

        return ResponseEntity.ok(MemberListResponse.from(members));
    }

    /**
     * 회원 정보 조회 By Nickname (타인 조회용)
     */
    @GetMapping("/{nickname}")
    public ResponseEntity<MemberPerformanceResponse> findMember(@PathVariable("nickname") String nickname) {

        Member member = memberService.findMemberByNickname(nickname);

        return ResponseEntity.ok(MemberPerformanceResponse.from(member));
    }

    /**
     * 회원 정보 조회 By MyProfile (본인 조회용)
     */
    @GetMapping("/profile")
    public ResponseEntity<MemberProfileResponse> myProfile(@AuthenticationPrincipal UserDetails userDetails) {

        Member member = memberService.findMemberByNickname(userDetails.getUsername());

        return ResponseEntity.ok(MemberProfileResponse.from(member));
    }

    /**
     * 회원 비밀번호 변경
     */
    @PostMapping("/{nickname}/password")
    public ResponseEntity<Void> changePassword(
        @PathVariable("nickname") String nickname,
        @RequestBody String password) {
        Member updatedMember = memberService.changePassword(nickname, password);
        return ResponseEntity.ok().build();
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> withdraw(@PathVariable("id") Long id) {
        memberService.delete(id);
        return ResponseEntity.ok().build();
    }
}