package algo_arena.member.controller;

import algo_arena.member.dto.request.MemberRegisterRequest;
import algo_arena.member.dto.request.MemberAuthRequest;
import algo_arena.member.dto.request.AuthEmailSendRequest;
import algo_arena.member.dto.request.AuthEmailVerifyRequest;
import algo_arena.member.dto.response.MemberRegisterResponse;
import algo_arena.member.dto.response.MemberAuthResponse;
import algo_arena.member.entity.Member;
import algo_arena.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {

    private final AuthService authService;

    /**
     * 인증코드 이메일 전송
     */
    @PostMapping("/email/send")
    public ResponseEntity<Void> sendAuthEmail(@RequestBody AuthEmailSendRequest request) {

        authService.sendAuthEmail(request.getEmail());

        return ResponseEntity.ok().build();
    }

    /**
     * 인증코드 확인(이메일 인증)
     */
    @PostMapping("/email/verify")
    public ResponseEntity<Void> verifyAuthEmail(@RequestBody AuthEmailVerifyRequest request) {

        authService.verifyAuthEmail(request.getEmail(), request.getAuthCode());

        return ResponseEntity.ok().build();
    }

    /**
     * 회원 가입
     */
    @PostMapping("/register")
    public ResponseEntity<MemberRegisterResponse> register(@RequestBody MemberRegisterRequest request) {

        Member newMember = authService.register(request.toEntity());

        return ResponseEntity.ok(MemberRegisterResponse.from(newMember));
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<MemberAuthResponse> login(@RequestBody MemberAuthRequest request) {

        final String token = authService.login(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(MemberAuthResponse.from(token));
    }
}