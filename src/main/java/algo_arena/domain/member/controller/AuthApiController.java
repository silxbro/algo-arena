package algo_arena.domain.member.controller;

import algo_arena.domain.member.dto.request.AuthEmailVerifyRequest;
import algo_arena.domain.member.dto.request.MemberRegisterRequest;
import algo_arena.domain.member.dto.response.MemberRegisterResponse;
import algo_arena.domain.member.entity.Member;
import algo_arena.domain.member.service.AuthService;
import algo_arena.domain.member.dto.request.MemberAuthRequest;
import algo_arena.domain.member.dto.request.AuthEmailSendRequest;
import algo_arena.domain.member.dto.response.MemberAuthResponse;
import jakarta.validation.Valid;
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
    public ResponseEntity<Void> sendAuthEmail(@Valid @RequestBody AuthEmailSendRequest request) {

        authService.sendAuthEmail(request.getEmail());

        return ResponseEntity.ok().build();
    }

    /**
     * 인증코드 확인(이메일 인증)
     */
    @PostMapping("/email/verify")
    public ResponseEntity<Void> verifyAuthEmail(@Valid @RequestBody AuthEmailVerifyRequest request) {

        authService.verifyAuthEmail(request.getEmail(), request.getAuthCode());

        return ResponseEntity.ok().build();
    }

    /**
     * 회원 가입
     */
    @PostMapping("/register")
    public ResponseEntity<MemberRegisterResponse> register(@Valid @RequestBody MemberRegisterRequest request) {

        Member newMember = authService.register(request.toEntity(), request.getConfirmPassword());

        return ResponseEntity.ok(MemberRegisterResponse.from(newMember));
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<MemberAuthResponse> login(@Valid @RequestBody MemberAuthRequest request) {

        final String token = authService.login(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(MemberAuthResponse.from(token));
    }
}