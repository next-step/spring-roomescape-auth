package roomescape.auth.ui;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.auth.application.AuthService;
import roomescape.auth.ui.annotation.Login;
import roomescape.auth.ui.dto.LoginCheckResponse;
import roomescape.auth.ui.dto.LoginMember;
import roomescape.auth.ui.dto.LoginRequest;

@Controller
@RequestMapping("login")
public class LoginController {
    private final AuthService authService;

    public LoginController(AuthService loginService) {
        this.authService = loginService;
    }

    @GetMapping
    public String readLoginPage() {
        return "login";
    }

    @PostMapping
    public void login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        Cookie cookie = authService.login(loginRequest);
        response.addCookie(cookie);
    }

    @GetMapping("check")
    public ResponseEntity<LoginCheckResponse> readMemberName(@Login LoginMember loginMember) {
        LoginCheckResponse response = new LoginCheckResponse(loginMember.name());
        return ResponseEntity.ok().body(response);
    }
}
