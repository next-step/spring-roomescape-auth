package roomescape.web.controller;

import java.net.URI;
import java.util.List;

import roomescape.service.ReservationService;
import roomescape.web.controller.dto.ReservationAdminRequest;
import roomescape.web.controller.dto.ReservationResponse;
import roomescape.web.controller.dto.ReservationSearchRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/admin/reservations")
public class ReservationAdminController {

	private final ReservationService reservationService;

	ReservationAdminController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}

	@PostMapping
	public ResponseEntity<ReservationResponse> createByAdmin(@RequestBody ReservationAdminRequest request) {
		ReservationResponse response = this.reservationService.createByAdmin(request);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(response.id())
			.toUri();

		return ResponseEntity.created(location).body(response);
	}

	@GetMapping("/search")
	public ResponseEntity<List<ReservationResponse>> searchReservations(ReservationSearchRequest request) {
		ReservationSearchRequest.validateSearchRequest(request);
		List<ReservationResponse> reservations = this.reservationService.searchReservations(request);
		return ResponseEntity.ok(reservations);
	}

}
