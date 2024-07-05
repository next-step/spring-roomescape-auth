package roomescape.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthPageController {

    @GetMapping("/login")
    public String reservation() {
        return "/login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "/signup";
    }
}
