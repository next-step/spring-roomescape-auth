package roomescape.domain.reservation.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.reservation.api.dto.ReservationRequest;
import roomescape.domain.reservation.api.dto.ReservationResponse;
import roomescape.domain.reservation.domain.Reservation;
import roomescape.domain.reservation.service.ReservationService;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.theme.service.ThemeService;
import roomescape.domain.time.domain.Time;
import roomescape.domain.time.service.TimeService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservations")
public class ApiReservationController {

    private final ReservationService reservationService;
    private final TimeService timeService;
    private final ThemeService themeService;

    public ApiReservationController(ReservationService reservationService, TimeService timeService, ThemeService themeService) {
        this.reservationService = reservationService;
        this.timeService = timeService;
        this.themeService = themeService;
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
                        reservation.getTheme()
                )).collect(Collectors.toList());
        return ResponseEntity.ok().body(responses);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> save(@RequestBody ReservationRequest request) {
        Time time = timeService.findById(request.getTimeId());
        Theme theme = themeService.findById(request.getThemeId());
        Reservation reservation = reservationService.save(new Reservation(null, request.getName(), request.getDate(), time, theme));
        return ResponseEntity.ok().body(
                new ReservationResponse(
                        reservation.getId(),
                        reservation.getName(),
                        reservation.getDate(),
                        reservation.getTime(),
                        reservation.getTheme()
                ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        reservationService.delete(id);
        return ResponseEntity.ok().build();
    }
}
