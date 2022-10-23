package nextstep.application.controller;

import nextstep.application.dto.auth.AuthRequest;
import nextstep.application.dto.auth.AuthResponse;
import nextstep.application.service.auth.AuthCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthCommandService authCommandService;

    public AuthController(AuthCommandService authCommandService) {
        this.authCommandService = authCommandService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authCommandService.login(request);
        return ResponseEntity.ok(response);
    }
}
