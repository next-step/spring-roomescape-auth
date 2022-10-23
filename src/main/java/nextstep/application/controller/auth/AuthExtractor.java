package nextstep.application.controller.auth;

import javax.servlet.http.HttpServletRequest;
import nextstep.common.exception.AuthException;
import org.springframework.stereotype.Component;

@Component
public class AuthExtractor {

    public String extract(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (authorization.startsWith("Bearer")) {
            return authorization.split(" ")[1];
        }
        throw new AuthException("유효하지 않은 토큰입니다.");
    }
}
