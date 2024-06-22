package roomescape.domain.reservation.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.argumentResolver.annotation.Login;
import roomescape.domain.member.domain.Member;
import roomescape.domain.reservation.domain.Reservation;
import roomescape.domain.reservation.service.ReservationService;
import roomescape.domain.reservation.service.dto.ReservationRequest;
import roomescape.domain.reservation.service.dto.ReservationResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservations")
public class ApiReservationController {

    private final ReservationService reservationService;

    public ApiReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<Reservation> reservations = reservationService.findAll();
        List<ReservationResponse> responses = reservations.stream().map(reservation ->
                new ReservationResponse(
                        reservation.getId(),
                        reservation.getName(),
                        reservation.getDate(),
                        reservation.getTime(),
                        reservation.getTheme(),
                        reservation.getMember())).collect(Collectors.toList());
        return ResponseEntity.ok().body(responses);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> save(@Login Member loginMember, @RequestBody ReservationRequest reservationRequest) {
        Reservation reservation = reservationService.save(reservationRequest, loginMember);
        return ResponseEntity.ok().body(
                new ReservationResponse(
                        reservation.getId(),
                        reservation.getName(),
                        reservation.getDate(),
                        reservation.getTime(),
                        reservation.getTheme(),
                        reservation.getMember()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        reservationService.delete(id);
        return ResponseEntity.ok().build();
    }
}
