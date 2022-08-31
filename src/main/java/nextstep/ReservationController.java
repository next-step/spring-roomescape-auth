package nextstep;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ReservationController {
    public List<Reservation> reservations = new ArrayList<>();

    @PostMapping("/reservations")
    public ResponseEntity addReservation(@RequestBody ReservationRequest reservationRequest) {
        Reservation reservation = new Reservation(
                LocalDate.parse(reservationRequest.getDate()),
                LocalTime.parse(reservationRequest.getTime() + ":00"),
                reservationRequest.getName()
        );
        reservations.add(reservation);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/reservations")
    public ResponseEntity showReservations(@RequestParam String date) {
        List<Reservation> result = reservations.stream()
                .filter(it -> it.getDate().isEqual(LocalDate.parse(date)))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/reservations")
    public ResponseEntity deleteReservation(@RequestParam String date, @RequestParam String time) {
        reservations.stream()
                .filter(it -> it.getDate().isEqual(LocalDate.parse(date)) && it.getTime().equals(LocalTime.parse(time + ":00")))
                .findFirst()
                .ifPresent(reservations::remove);

        return ResponseEntity.noContent().build();
    }
}
