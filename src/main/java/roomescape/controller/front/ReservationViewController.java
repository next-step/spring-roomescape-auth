package roomescape.controller.front;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReservationViewController {
    @GetMapping("/reservation")
    public String reservation() {
        return "/reservation";
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "/signup";
    }
}