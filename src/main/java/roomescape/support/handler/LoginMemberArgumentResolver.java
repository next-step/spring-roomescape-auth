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
import roomescape.support.ServletRequestTokenFinder;

public record LoginMemberArgumentResolver(
        JwtTokenManager jwtTokenManager
) implements HandlerMethodArgumentResolver {

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
        if (webRequest instanceof HttpServletRequest httpServletRequest) {
            String token = ServletRequestTokenFinder.getTokenByRequestCookies(httpServletRequest);
            jwtTokenManager.validateToken(token);
            return jwtTokenManager.getMemberEmailAndNameBy(token);
        }

        throw new IllegalArgumentException("HttpServletRequest 객체를 가져올 수 없습니다.");
    }
}
