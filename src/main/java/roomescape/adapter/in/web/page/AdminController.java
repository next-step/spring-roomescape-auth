package roomescape.adapter.in.web.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

  @GetMapping("/reservation")
  public String getAdminReservationPage() {
    return "admin/reservation";
  }

  @GetMapping("/theme")
  public String getAdminThemePage() {
    return "admin/theme";
  }
}
