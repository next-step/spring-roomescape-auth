package roomescape.argumentresolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.dto.auth.AuthCheckResponse;
import roomescape.exception.ErrorCode;
import roomescape.exception.custom.AuthorizationException;
import roomescape.jwt.JwtTokenProvider;
import roomescape.service.AuthService;
import roomescape.util.CookieUtil;

import java.util.Optional;


public class LoginArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;
    public LoginArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
        String token = CookieUtil.extractTokenFromCookie(request.getCookies())
                .orElseThrow(() -> new AuthorizationException(ErrorCode.UNAUTHORIZED_USER, "로그인한 유저만 이용가능합니다."));
        return authService.findUserFromToken(token);
    }
}
