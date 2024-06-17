package roomescape.config.auth.resolver;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.annotation.UserInfo;
import roomescape.application.dto.MemberResponse;
import roomescape.application.port.in.LoginUseCase;
import roomescape.exception.AuthenticationException;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final LoginUseCase loginUseCase;

    public LoginUserArgumentResolver(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserInfo.class);
    }

    @Override
    public MemberResponse resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                          NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
        throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String jwt = Arrays.stream(request.getCookies())
                           .filter(cookie -> cookie.getName().equals("jwt"))
                           .findFirst()
                           .orElseThrow(AuthenticationException::new)
                           .getValue();

        return loginUseCase.findMemberByToken(jwt);
    }
}
