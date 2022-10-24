package nextstep.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthorizationExtractor {

    public AuthorizationExtractor() { }

    public String extractAccessToken(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !(authorization.contains("Bearer"))) {
            throw new AuthenticationException();
        }

        return authorization.replace("Bearer ", "").trim();
    }
}
