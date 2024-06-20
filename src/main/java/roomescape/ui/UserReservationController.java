package roomescape.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserReservationController {
    @GetMapping("reservation")
    public String readUserReservation() {
        return "reservation";
    }
}
