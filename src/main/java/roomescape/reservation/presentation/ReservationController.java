package roomescape.reservation.presentation;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import roomescape.reservation.dto.ReservationCreateRequest;
import roomescape.auth.LoginUser;
import roomescape.reservation.application.ReservationService;
import roomescape.reservation.dto.UserReservationCreateRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.user.domain.User;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> saveReservation(@RequestBody @Valid UserReservationCreateRequest request,
                                                               @LoginUser User user) {
        ReservationCreateRequest reservationCreateRequest = request.toReservationCreateRequest(user.getId());
        ReservationResponse response = reservationService.save(reservationCreateRequest, user.getId());
        return ResponseEntity.created(URI.create("/reservations/" + response.id())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<ReservationResponse> responses = reservationService.getReservations();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.noContent().build();
    }
}
