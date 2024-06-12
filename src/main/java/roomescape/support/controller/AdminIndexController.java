package roomescape.support.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminIndexController {

    @GetMapping
    public String adminIndex() {
        return "/admin/index";
    }

    @GetMapping("/reservation")
    public String adminReservationPage() {
        return "/admin/reservation";
    }

    @GetMapping("/time")
    public String adminTimePage() {
        return "/admin/time";
    }

    @GetMapping("/theme")
    public String adminThemes() {
        return "/admin/theme";
    }
}
