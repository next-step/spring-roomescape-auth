package roomescape.web.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {

    @GetMapping("/")
    public String home() {
        return "login";
    }

    @GetMapping("/signup")
    public String toSignUpPage() {
        return "signup";
    }

    @GetMapping("/login")
    public String toLoginPage() {
        return "login";
    }

    @GetMapping("/customer/reservation")
    public String customerReservation() {
        return "customer/customer-reservation";
    }

    @GetMapping("/admin/reservation")
    public String reservation() {
        return "admin/admin-reservation";
    }

    @GetMapping("/admin/time")
    public String toTimePage() {
        return "admin/time";
    }

    @GetMapping("/admin/theme")
    public String themePage() {
        return "admin/theme";
    }
}
