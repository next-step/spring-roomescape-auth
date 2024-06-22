package roomescape.auth.ui;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.application.LoginService;
import roomescape.auth.ui.dto.LoginCheckResponse;
import roomescape.auth.ui.dto.LoginRequest;

@RestController
@RequestMapping("login")
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public void login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        Cookie cookie = loginService.login(loginRequest);
        response.addCookie(cookie);
    }

    @GetMapping("check")
    public ResponseEntity<LoginCheckResponse> readMemberName(HttpServletRequest request) {
        Long memberId = loginService.getMemberIdFromCookies(request.getCookies());
        LoginCheckResponse response = loginService.findMemberById(memberId);
        return ResponseEntity.ok().body(response);
    }
}
