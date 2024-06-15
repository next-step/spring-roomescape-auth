package roomescape.auth.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import roomescape.auth.dto.LoginRequest;
import roomescape.auth.application.AuthService;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String accessToken = authService.login(loginRequest);
        Cookie cookie = createCookie(accessToken);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    private Cookie createCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
