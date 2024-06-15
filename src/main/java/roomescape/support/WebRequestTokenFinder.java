package roomescape.support;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;
import roomescape.apply.auth.application.exception.TokenNotFoundException;

public class WebRequestTokenFinder {

    private static final String COOKIE = "cookie";
    private static final String TOKEN_NAME = "token=";

    private WebRequestTokenFinder() {
        throw new UnsupportedOperationException("인스턴스화 할 수 없는 클래스입니다.");
    }

    public static String getTokenByRequestCookies(NativeWebRequest webRequest) {
        String cookieHeader = webRequest.getHeader(COOKIE);
        if (!StringUtils.hasText(cookieHeader)) {
            throw new TokenNotFoundException();
        }
        return cookieHeader.replaceFirst(TOKEN_NAME, "");
    }

}
