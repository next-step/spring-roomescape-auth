package nextstep.auth;

import org.springframework.web.context.request.NativeWebRequest;

public interface AuthorizationExtractor {

    String extract(NativeWebRequest webRequest);
}
