package roomescape.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthViewController {
    @GetMapping
    public String login() {
        return "login";
    }

    @GetMapping("signup")
    public String signUp() {
        return "signup";
    }
}
