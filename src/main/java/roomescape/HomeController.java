package roomescape;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/admin/reservation")
    public String adminReservation(Model model) {
        return "admin/reservation";
    }

    @GetMapping("/admin/time")
    public String time(Model model) {
        return "admin/time";
    }

    @GetMapping("/admin/theme")
    public String theme(final Model model) {
        return "admin/theme";
    }

    @GetMapping("/reservation")
    public String reservation(final Model model) { return "reservation"; }

    @GetMapping("/login")
    public String goLoginPage(Model model) { return "login"; }
}
