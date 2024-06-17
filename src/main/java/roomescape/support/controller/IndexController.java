package roomescape.support.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @GetMapping
    public String index() {
        return "/index";
    }

    @GetMapping("/reservation")
    public String reservationPage() {
        return "/reservation";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "redirect", required = false) String redirectUrl,
                            Model model
    ) {
        model.addAttribute("redirectUrl", redirectUrl);
        return "/login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "/signup";
    }

}
