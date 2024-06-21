package roomescape.web.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReservationUIController {

	@GetMapping("/")
	public String reservationMain() {
		return "/index";
	}

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
