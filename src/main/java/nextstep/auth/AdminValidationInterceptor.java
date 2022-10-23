package nextstep.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.member.Member;
import nextstep.member.MemberService;
import nextstep.support.AuthorizationException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminValidationInterceptor implements HandlerInterceptor {

    private JwtTokenProvider jwtTokenProvider;
    private MemberService memberService;

    public AdminValidationInterceptor(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.contains("Bearer")) {
            throw new AuthenticationException();
        }
        String token = authorization.replace("Bearer ", "").trim();
        String username = jwtTokenProvider.getPrincipal(token);
        Member member = memberService.findByUsername(username);
        if (!member.hasAdminRole()) {
            throw new AuthorizationException();
        }
        return true;
    }
}
