package nextstep.auth;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;

@Component
public class BearerTokenRequestAuthorization implements RequestAuthorization {

    private static final String SCHEME_TYPE = "Bearer";

    @Override
    public String token(NativeWebRequest webRequest) {
        final String authorization = webRequest.getHeader("Authorization");
        return extract(authorization);
    }

    @Override
    public String token(HttpServletRequest webRequest) {
        final String authorization = webRequest.getHeader("Authorization");
        return extract(authorization);
    }

    private String extract(String authorization) {
        if (Objects.isNull(authorization) || !authorization.contains(SCHEME_TYPE)) {
            throw new AuthenticationException("Invalid token");
        }
        final int tokenIndex = authorization.indexOf(SCHEME_TYPE) + SCHEME_TYPE.length() + 1;
        return authorization.substring(tokenIndex);
    }
}
