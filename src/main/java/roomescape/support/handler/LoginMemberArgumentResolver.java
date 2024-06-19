package roomescape.support.handler;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.apply.auth.application.JwtTokenManager;
import roomescape.apply.auth.ui.dto.LoginMember;
import roomescape.apply.member.application.MemberFinder;
import roomescape.support.ServletRequestTokenFinder;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenManager jwtTokenManager;
    private final MemberFinder memberFinder;

    public LoginMemberArgumentResolver(JwtTokenManager jwtTokenManager, MemberFinder memberFinder) {
        this.jwtTokenManager = jwtTokenManager;
        this.memberFinder = memberFinder;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public LoginMember resolveArgument(@Nonnull MethodParameter parameter,
                                       ModelAndViewContainer mavContainer,
                                       @Nonnull NativeWebRequest webRequest,
                                       WebDataBinderFactory binderFactory
    ) {
        final HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        if (httpServletRequest != null) {
            String token = ServletRequestTokenFinder.getTokenByRequestCookies(httpServletRequest);
            jwtTokenManager.validateToken(token);
            final String email = jwtTokenManager.getEmailBy(token);
            return memberFinder.getLoginMemberByEmail(email);
        }
        throw new IllegalArgumentException("HttpServletRequest 객체를 가져올 수 없습니다.");
    }
}
