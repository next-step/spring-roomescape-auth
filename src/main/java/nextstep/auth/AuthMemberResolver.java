package nextstep.auth;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class AuthMemberResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthMemberResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String token = parseToken((HttpServletRequest) webRequest.getNativeRequest());
        jwtTokenProvider.validateToken(token);
        String principal = jwtTokenProvider.getPrincipal(token);
        return AuthMember.from(principal);
    }

    private String parseToken(HttpServletRequest request) {
        try {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            return header.split(" ")[1];
        } catch (Exception e) {
            throw new AuthenticationException();
        }
    }
}
