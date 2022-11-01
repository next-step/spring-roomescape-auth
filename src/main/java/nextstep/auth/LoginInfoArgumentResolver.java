package nextstep.auth;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginInfoArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenService tokenService;
    private final BearerTokenRequestAuthorization tokenAuthorizationExtractor;

    public LoginInfoArgumentResolver(
            TokenService tokenService,
            BearerTokenRequestAuthorization tokenAuthorizationExtractor
    ) {
        this.tokenService = tokenService;
        this.tokenAuthorizationExtractor = tokenAuthorizationExtractor;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public LoginInfo resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        final String token = tokenAuthorizationExtractor.token(webRequest);
        return tokenService.loginInfoFromToken(token);
    }
}
