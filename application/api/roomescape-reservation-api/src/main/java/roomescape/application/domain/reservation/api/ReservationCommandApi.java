package roomescape.application.domain.reservation.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.application.domain.reservation.api.dto.request.CreateReservationRequest;
import roomescape.application.domain.reservation.api.dto.response.CreateReservationResponse;
import roomescape.application.domain.reservation.service.ReservationCommandService;
import roomescape.application.domain.reservation.service.command.DeleteReservationCommand;
import roomescape.domain.reservation.Reservation;

import java.net.URI;

@RestController
public class ReservationCommandApi {

    private final ReservationCommandService reservationCommandService;

    public ReservationCommandApi(ReservationCommandService reservationCommandService) {
        this.reservationCommandService = reservationCommandService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<CreateReservationResponse> createReservation(@RequestBody @Valid CreateReservationRequest request) {
        Reservation reservation = reservationCommandService.create(request.toCreateReservationCommand());

        return ResponseEntity.created(URI.create(String.format("/reservations/%d", reservation.getId().id())))
                .body(CreateReservationResponse.from(reservation));
    }

    @DeleteMapping("/reservations/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
        reservationCommandService.delete(new DeleteReservationCommand(reservationId));

        return ResponseEntity.noContent().build();
    }
}
