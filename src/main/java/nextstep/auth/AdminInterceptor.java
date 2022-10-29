package nextstep.auth;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final RequestAuthorization tokenRequestAuthorization;
    private final TokenService tokenService;

    public AdminInterceptor(RequestAuthorization tokenRequestAuthorization, TokenService tokenService) {
        this.tokenRequestAuthorization = tokenRequestAuthorization;
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final String token = tokenRequestAuthorization.token(request);
        final LoginInfo loginInfo = tokenService.loginInfoFromToken(token);
        if(loginInfo.isAdmin()) {
            return true;
        }
        throw new AuthenticationException("관리자 기능을 보호한다.");
    }
}
