package roomescape.support.handler;

import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.apply.auth.application.JwtTokenManager;
import roomescape.apply.auth.application.exception.TokenNotFoundException;
import roomescape.apply.auth.ui.dto.LoginMember;
import roomescape.support.WebRequestTokenFinder;

public record LoginMemberArgumentResolver(
        JwtTokenManager jwtTokenManager
) implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory
    ) {
        String token = WebRequestTokenFinder.getTokenByRequestCookies(webRequest);
        jwtTokenManager.validateToken(token);
        return jwtTokenManager.getMemberEmailAndNameBy(token);
    }
}