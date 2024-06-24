package roomescape.argumentResolver;

import com.sun.jdi.InternalException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.argumentResolver.annotation.Login;
import roomescape.domain.member.error.exception.MemberErrorCode;
import roomescape.domain.member.error.exception.MemberException;
import roomescape.domain.member.service.MemberService;

import java.util.Arrays;

@Component
public class LoginArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String TOKEN = "token";

    private final MemberService memberService;

    public LoginArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest httpServletRequest = getHttpServletRequest(webRequest);
        Cookie[] cookies = getCookies(httpServletRequest);
        String token = extractTokenFromCookie(cookies);
        return memberService.findByToken(token);
    }

    private HttpServletRequest getHttpServletRequest(NativeWebRequest webRequest) {
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        if (httpServletRequest == null) {
            throw new InternalException();
        }
        return httpServletRequest;
    }

    private Cookie[] getCookies(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null) {
            throw new MemberException(MemberErrorCode.INVALID_MEMBER_DETAILS_ERROR);
        }
        return cookies;
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> TOKEN.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_COOKIE_ERROR));
    }
}
