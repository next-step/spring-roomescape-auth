package nextstep.auth;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginInfoArgumentResolver implements HandlerMethodArgumentResolver {

    private final LoginService loginService;
    private final BearerTokenAuthorizationExtractor tokenAuthorizationExtractor;

    public LoginInfoArgumentResolver(
            LoginService loginService,
            BearerTokenAuthorizationExtractor tokenAuthorizationExtractor
    ) {
        this.loginService = loginService;
        this.tokenAuthorizationExtractor = tokenAuthorizationExtractor;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public LoginInfo resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        final String token = tokenAuthorizationExtractor.extract(webRequest);
        return loginService.loginInfoFromToken(token);
    }
}
