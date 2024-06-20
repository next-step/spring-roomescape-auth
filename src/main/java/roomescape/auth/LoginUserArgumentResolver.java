package roomescape.auth;

import java.util.Arrays;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import roomescape.jwt.JwtTokenProvider;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String TOKEN_COOKIE_NAME = "token";

    private final JwtTokenProvider jwtTokenProvider;

    public LoginUserArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies);
        if (StringUtils.hasText(token)) {
            return jwtTokenProvider.getEmail(token);
        }
        return null;
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> TOKEN_COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse("");
    }
}
