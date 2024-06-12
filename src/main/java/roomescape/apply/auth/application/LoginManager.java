package roomescape.apply.auth.application;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import roomescape.apply.auth.ui.dto.LoginCheckResponse;
import roomescape.apply.auth.ui.dto.LoginResponse;
import roomescape.support.ServletRequestTokenFinder;

@Service
public class LoginManager {

    private final JwtTokenManager jwtTokenManager;

    public LoginManager(JwtTokenManager jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }


    public void addTokenToCookie(LoginResponse loginResponse, HttpServletResponse servletResponse) {
        String token = jwtTokenManager.generateToken(loginResponse);
        Cookie cookie = new Cookie(ServletRequestTokenFinder.COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        servletResponse.addCookie(cookie);
    }

    public LoginCheckResponse findRoleNameByToken(HttpServletRequest servletRequest) {
        String token = ServletRequestTokenFinder.getTokenByRequestCookies(servletRequest);
        jwtTokenManager.validateToken(token);
        return new LoginCheckResponse(jwtTokenManager.getRoleNameFromToken(token));
    }
}
