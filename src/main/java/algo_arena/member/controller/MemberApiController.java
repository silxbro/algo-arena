package algo_arena.member.controller;

import algo_arena.member.dto.request.PasswordChangeRequest;
import algo_arena.member.dto.response.MemberListResponse;
import algo_arena.member.dto.response.MemberPerformanceResponse;
import algo_arena.member.dto.response.MemberProfileResponse;
import algo_arena.member.entity.Member;
import algo_arena.member.service.MemberService;
import jakarta.validation.Valid;
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
    public ResponseEntity<MemberListResponse> findMembersByName(@RequestParam(value = "memberName", required = false) String memberName) {

        List<Member> members = memberService.findMembersByName(memberName);

        return ResponseEntity.ok(MemberListResponse.from(members));
    }

    /**
     * 회원 정보 조회 By Name (타인 조회용)
     */
    @GetMapping({"", "/{memberName}"})
    public ResponseEntity<MemberPerformanceResponse> findMember(@PathVariable(value = "memberName", required = false) String memberName) {

        Member member = memberService.findMemberByName(memberName);

        return ResponseEntity.ok(MemberPerformanceResponse.from(member));
    }

    /**
     * 회원 정보 조회 By MyProfile (본인 조회용)
     */
    @GetMapping("/profile")
    public ResponseEntity<MemberProfileResponse> findMyProfile(@AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();
        Member member = memberService.findMemberByName(username);

        return ResponseEntity.ok(MemberProfileResponse.from(member));
    }

    /**
     * 회원 비밀번호 변경
     */
    @PostMapping("/password")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal UserDetails userDetails,
        @Valid @RequestBody PasswordChangeRequest request) {

        String username = userDetails.getUsername();
        memberService.changePassword(username, request.getCurrentPassword(),
            request.getNewPassword(), request.getConfirmNewPassword());

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