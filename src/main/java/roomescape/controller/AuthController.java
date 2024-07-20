package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.argumentresolver.LoginUser;
import roomescape.domain.User;
import roomescape.dto.auth.AuthCheckResponse;
import roomescape.dto.auth.AuthLoginRequest;
import roomescape.dto.auth.AuthUserResponse;
import roomescape.dto.auth.UserResponse;
import roomescape.exception.ErrorCode;
import roomescape.exception.custom.AuthorizationException;
import roomescape.exception.custom.CookieNotFoundException;
import roomescape.service.AuthService;
import roomescape.util.CookieUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> findUsers() {
        return ResponseEntity.ok().body(authService.findUsers());
    }

    @PostMapping("/login")
    public ResponseEntity<Void> authLogin(@RequestBody AuthLoginRequest request,
                                          HttpServletResponse response) {
        String token = authService.authLogin(request);
        Cookie cookie = CookieUtil.createCookie(token);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/login/check")
    public ResponseEntity<AuthCheckResponse> checkLogin(@LoginUser User user) {
        return ResponseEntity.ok(authService.checkUser(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<Cookie> logout(@LoginUser User user, HttpServletResponse response) {
        Cookie cookie = CookieUtil.createCookie("");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

}
