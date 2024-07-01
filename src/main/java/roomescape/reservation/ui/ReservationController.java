package roomescape.reservation.ui;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.ui.annotation.Authenticated;
import roomescape.auth.ui.dto.LoginMember;
import roomescape.reservation.ui.dto.CookieReservationRequest;
import roomescape.reservation.ui.dto.ReservationRequest;
import roomescape.reservation.ui.dto.ReservationResponse;
import roomescape.reservation.application.ReservationService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> readAll() {
        List<ReservationResponse> reservations = reservationService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(reservations);
    }

    @GetMapping("{id}")
    public ResponseEntity<ReservationResponse> readOne(@PathVariable Long id) {
        ReservationResponse reservation = reservationService.findOne(id);
        return ResponseEntity.status(HttpStatus.OK).body(reservation);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(
            @RequestBody @Valid CookieReservationRequest cookieReservationRequest,
            @Authenticated LoginMember loginMember) {
        ReservationRequest request = ReservationRequest.fromCookieRequest(loginMember.name(), cookieReservationRequest);
        ReservationResponse reservation = reservationService.make(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/reservations/" + reservation.id()))
                .body(reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable("id") Long id) {
        reservationService.cancel(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
