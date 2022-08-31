package nextstep.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    public final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity createReservation(@RequestBody ReservationRequest reservationRequest) {
        reservationService.create(reservationRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity readReservations(@RequestParam String date) {
        List<Reservation> results = reservationService.readByDate(date);
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping
    public ResponseEntity deleteReservation(@RequestParam String date, @RequestParam String time) {
        reservationService.delete(date, time);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity onException() {
        return ResponseEntity.badRequest().build();
    }
}
