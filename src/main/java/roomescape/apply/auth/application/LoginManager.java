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
    private static final int COOKIE_EXPIRY = 3601;
    private static final String COOKIE_PATH = "/";
    private static final int EMPTY_EXPIRY = 0;
    private static final String EMPTY_TOKEN = null;

    private final JwtTokenManager jwtTokenManager;

    public LoginManager(JwtTokenManager jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }


    public void addTokenToCookie(LoginResponse loginResponse, HttpServletResponse servletResponse) {
        String token = jwtTokenManager.generateTokenByLoginResponse(loginResponse);
        Cookie cookie = new Cookie(ServletRequestTokenFinder.COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(COOKIE_EXPIRY);
        servletResponse.addCookie(cookie);
    }

    public LoginCheckResponse findRoleNameByToken(HttpServletRequest servletRequest) {
        String token = ServletRequestTokenFinder.getTokenByRequestCookies(servletRequest);
        jwtTokenManager.validateToken(token);
        return new LoginCheckResponse(jwtTokenManager.getRoleNameFromToken(token));
    }

    public void removeToken(HttpServletResponse servletResponse) {
        Cookie cookie = new Cookie(ServletRequestTokenFinder.COOKIE_NAME, EMPTY_TOKEN);
        cookie.setHttpOnly(true);
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(EMPTY_EXPIRY);
        servletResponse.addCookie(cookie);
    }
}
