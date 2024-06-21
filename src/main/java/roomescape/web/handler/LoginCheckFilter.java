package roomescape.web.handler;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.auth.JwtCookieManager;
import roomescape.auth.JwtTokenProvider;
import roomescape.exception.RoomEscapeException;

import org.springframework.util.ObjectUtils;

public class LoginCheckFilter implements Filter {

	private final JwtTokenProvider jwtTokenProvider;

	public LoginCheckFilter(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		Cookie[] cookies = httpRequest.getCookies();
		String token = JwtCookieManager.extractTokenFromCookie(cookies);

		if (ObjectUtils.isEmpty(token)) {
			httpResponse.sendRedirect("/login");
			return;
		}

		try {
			this.jwtTokenProvider.validateToken(token);
			chain.doFilter(request, response);
		}
		catch (IllegalArgumentException | RoomEscapeException ex) {
			httpResponse.sendRedirect("/login");
		}
	}

}
