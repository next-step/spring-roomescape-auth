package roomescape.member.infra;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.application.AuthService;
import roomescape.member.dto.MemberResponseDto;

import java.io.IOException;

import static roomescape.member.infra.TokenUtil.extractTokenFromCookie;

@Component
public class MemberHandlerInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberHandlerInterceptor.class);
    private final AuthService authService;

    public MemberHandlerInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        final Cookie[] cookies = request.getCookies();
        final String token = extractTokenFromCookie(cookies);
        final MemberResponseDto member = authService.findMember(token);

        LOGGER.info("check role of member : {}", member);

        if (member == null || !member.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
