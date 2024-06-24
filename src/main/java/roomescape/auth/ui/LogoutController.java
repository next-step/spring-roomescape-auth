package roomescape.auth.ui;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.application.LogoutService;

@RestController
@RequestMapping("logout")
public class LogoutController {
    private final LogoutService logoutService;

    public LogoutController(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    @PostMapping
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie token = logoutService.logout(request.getCookies());
        response.addCookie(token);
    }
}
