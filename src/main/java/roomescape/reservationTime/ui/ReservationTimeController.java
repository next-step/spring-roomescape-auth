package roomescape.reservationTime.ui;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservationTime.dto.ReservationTimeRequestDto;
import roomescape.reservationTime.dto.ReservationTimeResponseDto;
import roomescape.reservationTime.application.ReservationTimeService;

import java.util.List;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponseDto>> getTimes() {
        final List<ReservationTimeResponseDto> reservationTimeResponseDtos = reservationTimeService.getTimes();
        return ResponseEntity.ok().body(reservationTimeResponseDtos);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponseDto> addTime(@Valid @RequestBody ReservationTimeRequestDto reservationTimeRequestDto) {
        final ReservationTimeResponseDto time = reservationTimeService.addTime(reservationTimeRequestDto);
        return ResponseEntity.ok().body(time);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable Long id) {
        reservationTimeService.deleteTime(id);
        return ResponseEntity.ok().build();
    }
}
