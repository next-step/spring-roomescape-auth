package roomescape.web.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class ReservationAdminUIController {

	@GetMapping("/")
	public String admin() {
		return "/admin/index";
	}

	@GetMapping("/reservation")
	public String reservation() {
		return "/admin/reservation";
	}

	@GetMapping("/time")
	public String time() {
		return "/admin/time";
	}

	@GetMapping("/theme")
	public String theme() {
		return "/admin/theme";
	}

}
