package roomescape.auth.ui;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.application.LoginService;
import roomescape.auth.ui.dto.LoginRequest;

@RestController
@RequestMapping("login")
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public void create(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Cookie cookie = loginService.login(loginRequest);
        response.addCookie(cookie);
    }
}
