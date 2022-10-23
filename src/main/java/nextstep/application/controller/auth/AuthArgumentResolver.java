package nextstep.application.controller.auth;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import nextstep.application.service.auth.JwtTokenProvider;
import nextstep.common.exception.AuthException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthExtractor authExtractor;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthArgumentResolver(
        AuthExtractor authExtractor,
        JwtTokenProvider jwtTokenProvider
    ) {
        this.authExtractor = authExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Auth.class);
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) throws Exception {
        String token = authExtractor.extract((HttpServletRequest) webRequest.getNativeRequest());

        if (jwtTokenProvider.validateToken(token)) {
            String principal = jwtTokenProvider.getPrincipal(token);
            List<String> roles = jwtTokenProvider.getRoles(token);
            return new LoginMember(principal, roles);
        }
        throw new AuthException("권한이 없습니다. 로그인을 해주세요.");
    }
}
