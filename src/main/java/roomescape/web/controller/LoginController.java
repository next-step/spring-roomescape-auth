package roomescape.web.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.auth.JwtCookieManager;
import roomescape.service.AuthService;
import roomescape.web.controller.dto.LoginRequest;
import roomescape.web.controller.dto.LoginResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

	private static final int COOKIE_MAX_AGE = 3600;

	private final AuthService authService;

	LoginController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
		LoginRequest.validateLoginInfo(request);
		var token = this.authService.generateLoginToken(request);
		var createCookie = JwtCookieManager.createCookie(token, COOKIE_MAX_AGE);
		response.addCookie(createCookie);
		var loginResponse = this.authService.findRoleByToken(new Cookie[] { createCookie });
		return ResponseEntity.ok().body(loginResponse);
	}

	@GetMapping("/login/check")
	public ResponseEntity<LoginResponse> check(HttpServletRequest request) {
		return ResponseEntity.ok().body(this.authService.findRoleByToken(request.getCookies()));
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletResponse response) {
		var clearCookie = JwtCookieManager.clearCookie();
		response.addCookie(clearCookie);
		return ResponseEntity.ok().build();
	}

}
