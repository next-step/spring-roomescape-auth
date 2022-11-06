package nextstep.reservation;

import java.net.URI;
import java.util.List;
import nextstep.auth.MemberAuthentication;
import nextstep.member.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

  public final ReservationService reservationService;

  public ReservationController(ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  @PostMapping
  public ResponseEntity createReservation(@MemberAuthentication Member member,
      @RequestBody ReservationRequest reservationRequest) {
    Long id = reservationService.create(reservationRequest);
    return ResponseEntity.created(URI.create("/reservations/" + id)).build();
  }

  @GetMapping
  public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
    List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
    return ResponseEntity.ok().body(results);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity deleteReservation(@MemberAuthentication Member member, @PathVariable Long id) {
    reservationService.deleteById(id);

    return ResponseEntity.noContent().build();
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity onException(Exception e) {
    return ResponseEntity.badRequest().build();
  }
}
