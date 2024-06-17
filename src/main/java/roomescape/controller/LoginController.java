package roomescape.controller;

import java.util.Arrays;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.auth.JwtTokenProvider;
import roomescape.controller.dto.LoginCheckResponse;
import roomescape.controller.dto.LoginRequest;
import roomescape.controller.dto.MemberResponse;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.service.MemberService;
import roomescape.support.PasswordEncoder;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

	private static final int COOKIE_MAX_AGE = 3600;

	private static final String COOKE_PATH = "/";

	private static final String TOKEN = "token";

	private static final String ROLE = "role";

	private final MemberService memberService;

	private final JwtTokenProvider jwtTokenProvider;

	LoginController(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
		this.memberService = memberService;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@PostMapping("/login")
	public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {
		LoginRequest.validateLoginInfo(request);
		try {
			// TODO: STEP2 HandlerMethodArgumentResolver 를 통해서 리팩터링 예정
			LoginRequest loginRequest = new LoginRequest(request.email(), PasswordEncoder.encode(request.password()));
			MemberResponse memberResponse = this.memberService.findMemberByLoginRequest(loginRequest);

			String token = this.jwtTokenProvider.createToken(memberResponse);
			var cookie = this.createCookie(token, COOKIE_MAX_AGE);
			response.addCookie(cookie);

			return ResponseEntity.ok().build();
		}
		catch (RoomEscapeException ex) {
			if (ex.getErrorCode().equals(ErrorCode.NOT_FOUND_MEMBER)) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
		}

	}

	@GetMapping("/login/check")
	public ResponseEntity<LoginCheckResponse> check(HttpServletRequest request) {
		return ResponseEntity.ok().body(this.findRoleByToken(request.getCookies()));
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
		var tokenCookie = this.extractTokenFromCookie(request.getCookies());
		Cookie cookie = this.createCookie(tokenCookie, 0);
		response.addCookie(cookie);
		return ResponseEntity.ok().build();
	}

	// TODO: STEP2 에서 리팩터링 예정
	public Cookie createCookie(String token, int maxAge) {
		Cookie cookie = new Cookie(TOKEN, token);
		cookie.setHttpOnly(true);
		cookie.setPath(COOKE_PATH);
		cookie.setMaxAge(maxAge);
		return cookie;
	}

	private LoginCheckResponse findRoleByToken(Cookie[] cookies) {

		if (cookies == null || cookies.length == 0) {
			throw new RoomEscapeException(ErrorCode.NEEDS_LOGIN);
		}
		var token = extractTokenFromCookie(cookies);

		Claims role = this.jwtTokenProvider.validateToken(token);
		String roleName = role.get(ROLE, String.class);

		return new LoginCheckResponse(roleName);
	}

	private String extractTokenFromCookie(Cookie[] cookies) {
		if (cookies == null || cookies.length == 0) {
			return "";
		}

		return Arrays.stream(cookies)
			.filter((cookie) -> TOKEN.equals(cookie.getName()))
			.map(Cookie::getValue)
			.findFirst()
			.orElse("");
	}

}
