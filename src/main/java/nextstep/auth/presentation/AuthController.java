package nextstep.auth.presentation;

import nextstep.auth.AuthService;
import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.presentation.dto.TokenRequest;
import nextstep.auth.presentation.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<?> login(@RequestBody TokenRequest request) {
        try {
            return ResponseEntity.ok(
                new TokenResponse(authService.login(request.getUsername(), request.getPassword()))
            );

        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("잘못된 인증정보 입니다.");
        }
    }
}
