package roomescape.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.domain.Member;
import roomescape.domain.member.error.exception.MemberErrorCode;
import roomescape.domain.member.error.exception.MemberException;
import roomescape.domain.member.service.MemberService;

import java.util.Arrays;

public class RoleCheckInterceptor implements HandlerInterceptor {

    private static final String TOKEN = "token";

    private final MemberService memberService;

    public RoleCheckInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies);
        Member member = memberService.findByToken(token);
        if (member == null || member.isAdmin()) {
            throw new MemberException(MemberErrorCode.NOT_FOUND_COOKIE_ERROR);
        }
        return true;
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> TOKEN.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_COOKIE_ERROR));
    }
}
