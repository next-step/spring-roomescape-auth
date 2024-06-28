package roomescape.auth.ui;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.application.AuthService;

@RestController
@RequestMapping("logout")
public class LogoutController {
    private final AuthService authService;

    public LogoutController(AuthService logoutService) {
        this.authService = logoutService;
    }

    @PostMapping
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie token = authService.logout(request.getCookies());
        response.addCookie(token);
    }
}
