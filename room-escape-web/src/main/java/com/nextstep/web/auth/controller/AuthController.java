package com.nextstep.web.auth.controller;

import com.nextstep.web.auth.TokenRequest;
import com.nextstep.web.auth.TokenResponse;
import com.nextstep.web.auth.service.AuthMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("auth")
public class AuthController {

    private final AuthMemberService authMemberService;

    public AuthController(AuthMemberService authMemberService) {
        this.authMemberService = authMemberService;
    }

    @GetMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest request) {
        return ResponseEntity.ok(
                authMemberService.login(request)
        );
    }
}
