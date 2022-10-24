package nextstep.auth;

import java.util.Objects;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

@Component
public class BearerTokenAuthorizationExtractor implements AuthorizationExtractor {

    private static final String SCHEME_TYPE = "Bearer";

    @Override
    public String extract(NativeWebRequest webRequest) {
        final String authorization = webRequest.getHeader("Authorization");
        if(Objects.isNull(authorization) || !authorization.contains(SCHEME_TYPE)) {
            throw new AuthenticationException("Invalid token");
        }
        final int tokenIndex = authorization.indexOf(SCHEME_TYPE) + SCHEME_TYPE.length() + 1;
        return authorization.substring(tokenIndex);
    }
}
