package roomescape.auth.ui.argumentresolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.application.CookieUtils;
import roomescape.auth.application.JwtTokenProvider;
import roomescape.auth.ui.annotation.Login;
import roomescape.auth.ui.dto.LoginMember;
import roomescape.exception.UnauthorizedException;


public class LoginArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtils cookieUtils;

    public LoginArgumentResolver(JwtTokenProvider jwtTokenProvider, CookieUtils cookieUtils) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.cookieUtils = cookieUtils;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean isMemberLoginType = LoginMember.class.isAssignableFrom(parameter.getParameterType());
        return hasLoginAnnotation && isMemberLoginType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie[] cookies = request.getCookies();

        String token = cookieUtils
                .getCookieByName(cookies, "token")
                .orElseThrow(() -> UnauthorizedException.of("토큰이 없습니다."))
                .getValue();
        boolean isInvalidToken = !jwtTokenProvider.validateToken(token);

        if (isInvalidToken) {
            throw UnauthorizedException.of("토큰이 만료되었습니다.");
        }
        return new LoginMember(
                jwtTokenProvider.extractMemberId(token),
                jwtTokenProvider.extractMemberName(token)
        );
    }
}
