package nextstep.application.controller.reservation;

import java.net.URI;
import java.util.List;
import nextstep.application.dto.reservation.ReservationRequest;
import nextstep.application.dto.reservation.ReservationResponse;
import nextstep.application.service.reservation.ReservationCommandService;
import nextstep.application.service.reservation.ReservationQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationCommandService reservationCommandService;
    private final ReservationQueryService reservationQueryService;

    public ReservationController(
        ReservationCommandService reservationCommandService,
        ReservationQueryService reservationQueryService
    ) {
        this.reservationCommandService = reservationCommandService;
        this.reservationQueryService = reservationQueryService;
    }

    @PostMapping
    public ResponseEntity<Void> make(@RequestBody ReservationRequest request) {
        Long reservationId = reservationCommandService.make(request);
        return ResponseEntity.created(URI.create("/reservations/" + reservationId)).build();
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> checkAll(@RequestParam String date) {
        List<ReservationResponse> responses = reservationQueryService.checkAll(date);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping
    public ResponseEntity<Void> cancel(@RequestParam String date, @RequestParam String time) {
        reservationCommandService.cancel(date, time);
        return ResponseEntity.noContent().build();
    }
}
