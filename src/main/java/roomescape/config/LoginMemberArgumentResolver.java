package roomescape.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.domain.LoginMember;
import roomescape.domain.RoleType;
import roomescape.util.CookieUtils;
import roomescape.util.JwtTokenProvider;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String TOKEN = "token";
    private final JwtTokenProvider jwtTokenProvider;

    public LoginMemberArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer
            , NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Cookie[] cookies = httpServletRequest == null ? null : httpServletRequest.getCookies();

        String token = CookieUtils.extractCookieValue(cookies, TOKEN);

        Claims body = jwtTokenProvider.getClaimsFromToken(token);
        String email = jwtTokenProvider.getSubject(token);
        String name = String.valueOf(body.get("name"));
        RoleType role = RoleType.fromName(String.valueOf(body.get("role")));

        return new LoginMember(email, name, role);
    }
}
