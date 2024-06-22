package roomescape.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import roomescape.auth.JwtCookieManager;
import roomescape.auth.JwtTokenProvider;
import roomescape.domain.LoginMember;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.web.controller.dto.LoginRequest;
import roomescape.web.controller.dto.LoginResponse;
import roomescape.web.controller.dto.MemberResponse;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private static final String NAME = "name";

	private static final String ROLE = "role";

	private static final String EMAIL = "email";

	private final MemberService memberService;

	private final JwtTokenProvider jwtTokenProvider;

	public AuthService(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
		this.memberService = memberService;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	public String generateLoginToken(LoginRequest request) {
		LoginRequest.validateLoginInfo(request);
		MemberResponse memberResponse = this.memberService.findMemberByLoginRequest(request);
		return this.jwtTokenProvider.createToken(memberResponse);
	}

	public LoginResponse findRoleByToken(Cookie[] cookies) {
		Claims claims = extractClaimsFromCookies(cookies);
		String roleName = claims.get(ROLE, String.class);
		return new LoginResponse(roleName);
	}

	public LoginMember findMemberByToken(Cookie[] cookies) {
		Claims claims = extractClaimsFromCookies(cookies);
		String name = claims.get(NAME, String.class);
		String email = claims.get(EMAIL, String.class);
		String role = claims.get(ROLE, String.class);
		return LoginMember.builder().name(name).email(email).role(role).build();
	}

	private Claims extractClaimsFromCookies(Cookie[] cookies) {
		if (cookies == null || cookies.length == 0) {
			throw new RoomEscapeException(ErrorCode.NEEDS_LOGIN);
		}
		String token = JwtCookieManager.extractTokenFromCookie(cookies);
		return this.jwtTokenProvider.validateToken(token);
	}

}
