package nextstep.auth;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    public LoginMemberArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMemberPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        try {
            String token = parseToken(webRequest);
            String memberId = jwtTokenProvider.getPrincipal(token);
            return LoginMember.from(memberId);
        } catch (Exception e) {
            throw new AuthenticationException();
        }
    }

    private String parseToken(NativeWebRequest webRequest) {
        try {
            return webRequest.getHeader(HttpHeaders.AUTHORIZATION).split("Bearer ")[1];
        } catch (Exception e) {
            throw new AuthenticationException();
        }
    }
}
