package roomescape.web.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.domain.LoginMember;
import roomescape.domain.MemberRole;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.service.AuthService;

import org.springframework.web.servlet.HandlerInterceptor;

public class RoleInterceptor implements HandlerInterceptor {

	private final AuthService authService;

	public RoleInterceptor(AuthService authService) {
		this.authService = authService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String requestURI = request.getRequestURI();
		if (requestURI.startsWith("/admin")) {
			Cookie[] cookies = request.getCookies();
			LoginMember loginMember = this.authService.findMemberByToken(cookies);
			if (loginMember == null || !MemberRole.ADMIN.name().equals(loginMember.getRole())) {
				throw new RoomEscapeException(ErrorCode.FORBIDDEN);
			}
		}
		return true;
	}

}
