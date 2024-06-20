package roomescape.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import roomescape.auth.JwtCookieManager;
import roomescape.auth.JwtTokenProvider;
import roomescape.controller.dto.LoginCheckResponse;
import roomescape.controller.dto.LoginRequest;
import roomescape.controller.dto.MemberResponse;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.support.PasswordEncoder;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private static final String ROLE = "role";

	private final MemberService memberService;

	private final JwtTokenProvider jwtTokenProvider;

	public AuthService(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
		this.memberService = memberService;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	public String generateLoginToken(LoginRequest request) {
		LoginRequest.validateLoginInfo(request);
		String encodedPassword = PasswordEncoder.encode(request.password());
		LoginRequest loginRequest = new LoginRequest(request.email(), encodedPassword);
		MemberResponse memberResponse = this.memberService.findMemberByLoginRequest(loginRequest);
		return this.jwtTokenProvider.createToken(memberResponse);
	}

	public LoginCheckResponse findRoleByToken(Cookie[] cookies) {

		if (cookies == null || cookies.length == 0) {
			throw new RoomEscapeException(ErrorCode.NEEDS_LOGIN);
		}
		var token = JwtCookieManager.extractTokenFromCookie(cookies);

		Claims role = this.jwtTokenProvider.validateToken(token);
		String roleName = role.get(ROLE, String.class);

		return new LoginCheckResponse(roleName);
	}

}
