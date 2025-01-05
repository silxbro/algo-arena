package algo_arena.member.controller;

import algo_arena.member.dto.request.MemberCreateRequest;
import algo_arena.member.dto.response.MemberCreateResponse;
import algo_arena.member.dto.response.MemberDetailResponse;
import algo_arena.member.dto.response.MemberListResponse;
import algo_arena.member.dto.response.MemberUpdateResponse;
import algo_arena.member.entity.Member;
import algo_arena.member.service.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    private static final String UPDATE_IMAGE_MESSAGE = "프로필 이미지가 변경되었습니다";
    private static final String CHANGE_PASSWORD_MESSAGE = "비밀번호가 변경되었습니다.";
    private static final String WITHDRAW_MESSAGE = "회원 탈퇴가 완료되었습니다.";

    private final MemberService memberService;

    /**
     * 회원 가입
     */
    @PostMapping
    public ResponseEntity<MemberCreateResponse> register(@RequestBody MemberCreateRequest request) {

        Member newMember = memberService.saveMember(request.toEntity());

        return ResponseEntity.ok(MemberCreateResponse.from(newMember.getEmail()));
    }

    /**
     * 회원 정보 조회 (검색)
     */
    @GetMapping
    public ResponseEntity<MemberListResponse> findAllByNickname(@RequestParam("nickname") String nickname) {

        List<Member> members = memberService.findMembersNickname(nickname);

        return ResponseEntity.ok(MemberListResponse.from(members));
    }

    /**
     * 회원 정보 조회 (프로필)
     */
    @GetMapping("/{id}")
    public ResponseEntity<MemberDetailResponse> findOne(@PathVariable("id") Long id) {

        Member member = memberService.findMemberById(id);

        return ResponseEntity.ok(MemberDetailResponse.from(member));
    }

    /**
     * 회원 프로필 이미지 변경
     */
    @PatchMapping("/{id}/image")
    public ResponseEntity<MemberUpdateResponse> updateImage(
            @PathVariable("id") Long id,
            @RequestBody String imgUrl) {
        Member updatedMember = memberService.updateImage(id, imgUrl);
        return ResponseEntity.ok(MemberUpdateResponse.from(UPDATE_IMAGE_MESSAGE, updatedMember));
    }

    /**
     * 회원 비밀번호 변경
     */
    @PostMapping("/{id}/password")
    public ResponseEntity<MemberUpdateResponse> changePassword(
        @PathVariable("id") Long id,
        @RequestBody String password) {
        Member updatedMember = memberService.changePassword(id, password);
        return ResponseEntity.ok(MemberUpdateResponse.from(CHANGE_PASSWORD_MESSAGE, updatedMember));
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> withdraw(@PathVariable("id") Long id) {
        memberService.delete(id);
        return ResponseEntity.ok(WITHDRAW_MESSAGE);
    }
}