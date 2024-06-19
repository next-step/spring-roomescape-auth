package roomescape.adapter.in.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.annotation.UserInfo;
import roomescape.application.dto.AdminReservationCommand;
import roomescape.application.dto.MemberResponse;
import roomescape.application.port.in.ReservationUseCase;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ReservationUseCase reservationUseCase;

    public AdminController(ReservationUseCase reservationUseCase) {
        this.reservationUseCase = reservationUseCase;
    }

    @GetMapping("/reservation")
    public String getAdminReservationPage() {
        return "admin/reservation";
    }


    @PostMapping("/reservation")
    public String postAdminReservationPage(@UserInfo MemberResponse memberResponse, @RequestBody
    AdminReservationCommand adminReservationCommand) {

        reservationUseCase.registerAdminReservation(adminReservationCommand);

        return "admin/reservation";
    }

    @GetMapping("/time")
    public String getAdminTimePage() {
        return "admin/time";
    }

    @GetMapping("/theme")
    public String getAdminThemePage() {
        return "admin/theme";
    }
}
