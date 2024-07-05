package roomescape.application.domain.auth.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.domain.auth.api.dto.request.LoginRequest;
import roomescape.application.domain.auth.api.dto.response.LoginCheckResponse;
import roomescape.application.domain.auth.service.AuthService;
import roomescape.application.domain.auth.service.query.LoginCheckQuery;
import roomescape.domain.user.User;
import roomescape.jwt.JwtToken;
import roomescape.jwt.extractor.CookieTokenExtractor;

@RestController
public class AuthApi {

    private final AuthService authService;
    private final CookieTokenExtractor tokenExtractor;

    public AuthApi(AuthService authService, CookieTokenExtractor tokenExtractor) {
        this.authService = authService;
        this.tokenExtractor = tokenExtractor;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        JwtToken token = authService.login(request.toCommand());

        Cookie cookie = new Cookie("token", token.token());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> loginCheck(HttpServletRequest request) {
        User user = authService.loginCheck(new LoginCheckQuery(tokenExtractor.extract(request)));

        return ResponseEntity.ok(LoginCheckResponse.from(user));
    }
}
