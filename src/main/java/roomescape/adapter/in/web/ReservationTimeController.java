package roomescape.adapter.in.web;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.dto.ReservationTimeCommand;
import roomescape.application.dto.ReservationTimeResponse;
import roomescape.application.port.in.ReservationTimeUseCase;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

  private final ReservationTimeUseCase reservationTimeUseCase;

  public ReservationTimeController(ReservationTimeUseCase reservationTimeUseCase) {
    this.reservationTimeUseCase = reservationTimeUseCase;
  }

  @PostMapping
  public ResponseEntity<ReservationTimeResponse> createReservationTime(
    @RequestBody ReservationTimeCommand reservationTimeCommand) {
    return ResponseEntity.status(CREATED)
                         .body(reservationTimeUseCase.registerReservationTime(reservationTimeCommand));
  }

  @GetMapping
  public ResponseEntity<List<ReservationTimeResponse>> getReservationTimes() {
    return new ResponseEntity<>(reservationTimeUseCase.retrieveReservationTimes(), HttpStatus.OK);

  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("/{id}")
  public void deleteReservation(@PathVariable("id") Long id) {
    reservationTimeUseCase.deleteReservationTime(id);
  }

  @GetMapping("/available")
  public ResponseEntity<List<ReservationTimeResponse>> getAvailableReservationTimes(@RequestParam("date") String date,
    @RequestParam("themeId") Long themeId) {
    return new ResponseEntity<>(reservationTimeUseCase.retrieveAvailableReservationTimes(date, themeId), HttpStatus.OK);
  }
}
