package nextstep.auth.resolver;

import nextstep.auth.jwt.JwtTokenProvider;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.stream.Collectors;

import static nextstep.auth.resolver.LoginUser.Authority;

public class AuthResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        try {
            String authorization = webRequest.getHeader("authorization");
            String token = authorization.substring("Bearer".length()).trim();
            if (!jwtTokenProvider.validateToken(token)) {
                return LoginUser.anonymous();
            }

            JwtTokenProvider.TokenBody tokenBody = jwtTokenProvider.getTokenBody(token);
            return new LoginUser(
                tokenBody.getBody(),
                tokenBody.getRoles()
                    .stream()
                    .map(Authority::valueOf)
                    .collect(Collectors.toList())
            );
        } catch (Exception e) {
            return LoginUser.anonymous();
        }
    }
}
