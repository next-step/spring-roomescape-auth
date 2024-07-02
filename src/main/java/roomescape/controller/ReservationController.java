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
import roomescape.auth.LoginMember;
import roomescape.auth.MemberEmail;
import roomescape.config.TokenPropertiesConfig;
import roomescape.dto.ReservationAddRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.entities.Reservation;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;
import roomescape.service.ThemeService;

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
    @MemberEmail LoginMember loginMemberEmail) {
    String email = tokenPropertiesConfig.getEmailFromToken(loginMemberEmail.getToken());
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
