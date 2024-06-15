package roomescape.controller.front;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.LoginResponse;
import roomescape.service.MemberService;

@Controller
public class ReservationViewController {

    private final MemberService memberService;

    public ReservationViewController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/reservation")
    public String reservation() {
        return "/reservation";
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
        String tokenValue = memberService.tokenLogin(request);
        ResponseCookie responseCookie = ResponseCookie.from("token", tokenValue)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(60)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, responseCookie.toString());

        return ResponseEntity.ok()
                .headers(headers)
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginResponse> loginCheck(HttpServletRequest request) {
        String token = extractCookieValue(request, "token");

        LoginResponse loginResponse = memberService.loginCheck(token);
        return ResponseEntity.ok().body(loginResponse);
    }

    private String extractCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
