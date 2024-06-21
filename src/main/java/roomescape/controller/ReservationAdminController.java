package roomescape.controller;

import roomescape.controller.dto.ReservationAdminRequest;
import roomescape.controller.dto.ReservationResponse;
import roomescape.service.ReservationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/reservations")
public class ReservationAdminController {

	private final ReservationService reservationService;

	ReservationAdminController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}

	@PostMapping
	public ResponseEntity<ReservationResponse> createByAdmin(@RequestBody ReservationAdminRequest request) {
		return new ResponseEntity<>(this.reservationService.createByAdmin(request), HttpStatus.CREATED);
	}

}
