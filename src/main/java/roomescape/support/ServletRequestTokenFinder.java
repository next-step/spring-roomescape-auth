package roomescape.support;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import roomescape.apply.auth.application.exception.TokenNotFoundException;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class ServletRequestTokenFinder {

    public static final String COOKIE_NAME = "token";

    private ServletRequestTokenFinder() {
        throw new UnsupportedOperationException("인스턴스화 할 수 없는 클래스입니다.");
    }

    public static String getTokenByRequestCookies(HttpServletRequest servletRequest) {
        Cookie[] cookies = servletRequest.getCookies();
        if (cookies == null || cookies.length == 0) {
            throw new TokenNotFoundException();
        }

        return Arrays.stream(cookies)
                .filter(it -> COOKIE_NAME.equals(it.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

}
