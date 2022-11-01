package nextstep.auth;

import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;

public interface RequestAuthorization {

    String token(NativeWebRequest webRequest);

    String token(HttpServletRequest webRequest);
}
