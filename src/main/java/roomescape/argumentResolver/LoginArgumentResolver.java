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
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        if (httpServletRequest != null) {
            Cookie[] cookies = httpServletRequest.getCookies();
            if (cookies != null) {
                return memberService.findByToken(extractTokenFromCookie(cookies));
            }
            throw new MemberException(MemberErrorCode.INVALID_MEMBER_DETAILS_ERROR);
        }
        throw new InternalException();
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(TOKEN)) {
                return cookie.getValue();
            }
        }
        throw new MemberException(MemberErrorCode.NOT_FOUND_COOKIE_ERROR);
    }
}
