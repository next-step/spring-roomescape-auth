package roomescape.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.LoginUserEmail;
import roomescape.auth.UserEmail;
import roomescape.config.TokenPropertiesConfig;
import roomescape.domain.reservation.ReservationService;
import roomescape.domain.reservationtime.ReservationTimeService;
import roomescape.domain.theme.ThemeService;
import roomescape.dto.ReservationAddRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.entities.Reservation;
import roomescape.util.TokenUtil;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reservations")
public class ReservationController {
  private final ReservationService reservationService;
  private final ReservationTimeService reservationTimeService;
  private final ThemeService themeService;
  private final TokenPropertiesConfig tokenPropertiesConfig;

  @GetMapping
  public ResponseEntity<List<ReservationResponseDto>> findAllReservations(){
    List<ReservationResponseDto> res = reservationService.findAllReservations();
    return ResponseEntity.ok().body(res);
  }

  @PostMapping
  public ResponseEntity<Reservation> saveReservation(
    @RequestBody ReservationAddRequestDto reservationAddRequestDto,
    @UserEmail LoginUserEmail loginUserEmail) {
    String email = TokenUtil.getEmailFromToken(loginUserEmail.getToken(), tokenPropertiesConfig);
    Reservation reservation = reservationService.saveReservation(
      reservationAddRequestDto);

    return ResponseEntity.ok().body(reservation);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity cancelReservation(@PathVariable("id") Long id) {
    reservationService.cancelReservation(id);
    return ResponseEntity.ok().build();
  }
}
