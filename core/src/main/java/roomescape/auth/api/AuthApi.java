package roomescape.auth.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.api.response.LoginCheckResponse;
import roomescape.auth.application.AuthService;
import roomescape.auth.application.dto.LoginRequest;
import roomescape.auth.application.dto.LoginToken;
import roomescape.auth.application.dto.SignUpRequest;
import roomescape.auth.support.JwtSupporter;
import roomescape.domain.user.domain.User;
import roomescape.global.rest.ApiResponse;

@RestController
@RequiredArgsConstructor
public class AuthApi {

    private final AuthService authService;
    private final JwtSupporter jwtSupporter;

    @PostMapping("/api/signup")
    public ApiResponse<Void> signUp(@RequestBody SignUpRequest signUpRequest) {
        authService.signUp(signUpRequest);
        return ApiResponse.okWithEmptyData();
    }

    @PostMapping("/api/login")
    public ApiResponse<Void> login(
            final HttpServletResponse servletResponse,
            @RequestBody final LoginRequest loginRequest
    ) {
        final LoginToken loginToken = authService.createLoginToken(loginRequest);

        final HttpCookie cookie = ResponseCookie.from("accessToken", loginToken.accessToken())
                .path("/") // 쿠키의 경로는 루트 경로. 모든 경로에 대해 해당 쿠키가 클라이언트로부터 전달된다.
                .httpOnly(true) // JS를 통해 쿠키에 접근하지 못하게 설정 -> XSS(Cross-Site Scripting) 공격 방어
                .secure(false) // true로 설정 시 HTTPS 연결에서만 쿠키를 전송. (미션은 로컬 개발, HTTP를 사용하므로 false로 지정. 운영에서는 true권장)
                .sameSite("Strict") // CSRF 공격 방어. "Strict"로 설정 시, 현재 페이지와 동일한 도메인으로의 요청에만 쿠키가 전송된다.
                .build();
        servletResponse.addHeader("Set-Cookie", cookie.toString());

        return ApiResponse.okWithEmptyData();
    }

    @GetMapping("/api/login/check")
    public ApiResponse<LoginCheckResponse> loginCheck(HttpServletRequest servletRequest) {
        LoginToken loginToken = jwtSupporter.extractLoginToken(servletRequest);  // todo AOP를 이용한 LoginToken 주입 받기.
        User user = authService.getUserByLoginToken(loginToken); // todo 이미 로그인한 상태의 User를 AOP를 이용해서 주입 받기

        LoginCheckResponse response = new LoginCheckResponse(user.getName(), user.getRole());
        return ApiResponse.ok(response);
    }

    @PostMapping("/api/logout")
    public ApiResponse<Void> logout(final HttpServletResponse servletResponse) {
        final HttpCookie cookie = ResponseCookie.from("accessToken", "")
                .path("/")           // login과 동일한 path 설정
                .httpOnly(true)      // login과 동일한 httpOnly 설정
                .secure(false)       // login과 동일한 secure 설정
                .sameSite("Strict")  // login과 동일한 sameSite 설정
                .maxAge(0)          // 쿠키 즉시 만료
                .build();
        servletResponse.addHeader("Set-Cookie", cookie.toString());

        return ApiResponse.okWithEmptyData();
    }
}
