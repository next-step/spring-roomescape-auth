package nextstep.application.controller;

import nextstep.application.service.JwtTokenProvider;
import nextstep.application.dto.auth.AuthRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login/token")
    public ResponseEntity<Void> login(@RequestBody AuthRequest request) {


        return ResponseEntity.ok().build();
    }
}
