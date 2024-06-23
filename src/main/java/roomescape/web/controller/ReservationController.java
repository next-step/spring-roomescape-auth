package roomescape.web.controller;

import java.net.URI;
import java.util.List;

import roomescape.domain.LoginMember;
import roomescape.service.ReservationService;
import roomescape.web.controller.dto.ReservationRequest;
import roomescape.web.controller.dto.ReservationResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

	private final ReservationService reservationService;

	ReservationController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}

	@GetMapping
	public ResponseEntity<List<ReservationResponse>> getReservations() {
		return ResponseEntity.ok().body(this.reservationService.getReservations());
	}

	@PostMapping
	public ResponseEntity<ReservationResponse> create(@RequestBody ReservationRequest request,
			LoginMember loginMember) {
		ReservationResponse response = this.reservationService.create(request, loginMember);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(response.id())
			.toUri();

		return ResponseEntity.created(location).body(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> cancel(@PathVariable("id") long id) {
		this.reservationService.cancel(id);
		return ResponseEntity.noContent().build();
	}

}
