package roomescape.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.application.dto.MemberResponse;
import roomescape.application.port.in.LoginUseCase;
import roomescape.exception.AuthenticationException;
import roomescape.exception.AuthorizationException;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final LoginUseCase loginUseCase;

    public AdminInterceptor(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        String jwt = Arrays.stream(request.getCookies())
                           .filter(cookie -> cookie.getName().equals("jwt"))
                           .findFirst()
                           .orElseThrow(AuthenticationException::new)
                           .getValue();

        MemberResponse memberResponse = loginUseCase.findMemberByJwt(jwt);

        if (!memberResponse.role().name().equals("ADMIN")) {
            throw new AuthorizationException();
        }

        return true;
    }
}
