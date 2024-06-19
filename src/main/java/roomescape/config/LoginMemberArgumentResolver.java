package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.LoginMember;
import roomescape.domain.Member;
import roomescape.service.MemberService;
import roomescape.util.CookieUtils;
import roomescape.util.JwtTokenProvider;

import java.util.Objects;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginMemberArgumentResolver(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer
            , NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Cookie[] cookies = Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class)).getCookies();
        String token = CookieUtils.extractCookieValue(cookies, "token");

        String email = jwtTokenProvider.getPayload(token);
        Member member = memberService.findByEmail(email);

        return new LoginMember(member.getId(), member.getEmail(), member.getName());
    }
}
