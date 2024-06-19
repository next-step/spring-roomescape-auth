package roomescape.login;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping
    public String index() {
        return "login";
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
        String token = loginService.createToken(loginRequest.getEmail(), loginRequest.getPassword());

        ResponseCookie cookie = ResponseCookie
                                    .from("token", token)
                                    .path("/")
                                    .httpOnly(true)
                                    .secure(true)
                                    .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @GetMapping("/check")
    public ResponseEntity<LoginResponse> checkLogin(@CookieValue("token") String token) {
        return ResponseEntity.ok().body(loginService.checkToken(token));
    }
}
