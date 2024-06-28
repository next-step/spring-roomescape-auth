package roomescape.auth.ui;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.application.AuthService;
import roomescape.auth.ui.annotation.Authenticated;
import roomescape.auth.ui.dto.LoginMember;

@RestController
@RequestMapping("logout")
public class LogoutController {
    private final AuthService authService;

    public LogoutController(AuthService logoutService) {
        this.authService = logoutService;
    }

    @PostMapping
    public void logout(@Authenticated LoginMember loginMember, HttpServletResponse response) {
        Cookie token = authService.logout();
        response.addCookie(token);
    }
}
