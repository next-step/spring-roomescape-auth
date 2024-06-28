package roomescape.reservation.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.application.ReservationService;
import roomescape.reservationtime.dto.ReservationTimeResponseDto;

import java.util.List;

@RestController
public class AvailableTimesController {

    private final ReservationService reservationService;

    public AvailableTimesController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/times/available")
    public ResponseEntity<List<ReservationTimeResponseDto>> getAvaliableTimes(final @RequestParam String date, final @RequestParam Long themeId) {
        final List<ReservationTimeResponseDto> reservationTimeResponseDtos = reservationService.findAvailableTimes(date, themeId);
        return ResponseEntity.ok().body(reservationTimeResponseDtos);
    }
}
