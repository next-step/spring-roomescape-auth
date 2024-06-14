package roomescape.controller.front;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.LoginResponse;
import roomescape.service.UserService;

@Controller
public class ReservationViewController {

    private final UserService userService;

    public ReservationViewController(UserService userService) {
        this.userService = userService;
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
        HttpHeaders headers = new HttpHeaders();
        Cookie cookie = new Cookie("token", userService.tokenLogin(request));
        headers.add(HttpHeaders.SET_COOKIE, String.valueOf(cookie));

        return ResponseEntity.ok()
                .headers(headers)
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginResponse> loginCheck(HttpServletRequest request) {
        LoginResponse loginResponse = userService.loginCheck();
        return ResponseEntity.ok().build();
    }
}
