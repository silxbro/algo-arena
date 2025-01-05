package algo_arena.member.controller;

import algo_arena.member.dto.request.MemberCreateRequest;
import algo_arena.member.dto.request.MemberLoginRequest;
import algo_arena.member.dto.response.MemberCreateResponse;
import algo_arena.member.dto.response.MemberLoginResponse;
import algo_arena.member.entity.Member;
import algo_arena.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthApiController {

    private final MemberService memberService;

    /**
     * 회원 가입
     */
    @PostMapping("/register")
    public ResponseEntity<MemberCreateResponse> register(@RequestBody MemberCreateRequest request) {

        Member newMember = memberService.saveMember(request.toEntity());

        return ResponseEntity.ok(MemberCreateResponse.from(newMember.getEmail()));
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> login(@RequestBody MemberLoginRequest request) {

        final String token = memberService.authenticate(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(MemberLoginResponse.from(token));
    }
}