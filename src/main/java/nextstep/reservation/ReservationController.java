package nextstep.reservation;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import nextstep.auth.AuthenticationException;
import nextstep.auth.MemberAuthentication;
import nextstep.member.Member;
import nextstep.member.MemberService;
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
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

  private final ReservationService reservationService;
  private final MemberService memberService;

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
    var reservation = reservationService.findById(id);
    if (!Objects.equals(reservation.getName(), member.getName())) {
      throw new AuthenticationException("자신의 예약만 취소 가능합니다.");
    }
    reservationService.deleteById(id);

    return ResponseEntity.noContent().build();
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handle(RuntimeException exception) {
    return ResponseEntity.badRequest().body(exception.getMessage());
  }
}
