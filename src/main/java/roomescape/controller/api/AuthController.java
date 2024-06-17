package roomescape.controller.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.LoginResponse;
import roomescape.service.MemberService;
import roomescape.util.CookieUtils;

@RestController
public class AuthController {
    private static final String TOKEN_COOKIE_NAME = "token";
    private final MemberService memberService;

    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest request) {
        String tokenValue = memberService.tokenLogin(request);

        ResponseCookie responseCookie = CookieUtils.createResponseCookie(TOKEN_COOKIE_NAME, tokenValue
                , 60); // 60초 후 만료
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, responseCookie.toString());

        return ResponseEntity.ok().headers(headers).build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginResponse> loginCheck(HttpServletRequest request) {
        String token = CookieUtils.extractCookieValue(request.getCookies(), TOKEN_COOKIE_NAME);
        LoginResponse loginResponse = memberService.loginCheck(token);
        return ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = CookieUtils.expireCookieByName(request.getCookies(), TOKEN_COOKIE_NAME);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }
}
