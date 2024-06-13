package roomescape.time.presntation;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import roomescape.time.application.ReservationTimeService;
import roomescape.time.dto.ReservationTimeCreateRequest;
import roomescape.time.dto.ReservationTimeResponse;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> createReservationTime(@RequestBody @Valid ReservationTimeCreateRequest request) {
        ReservationTimeResponse response = reservationTimeService.createReservationTime(request);
        return ResponseEntity.created(URI.create("/times/" + response.id())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getReservationTimes() {
        List<ReservationTimeResponse> responses = reservationTimeService.getReservationTimes();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/available")
    public ResponseEntity<List<ReservationTimeResponse>> getAvailableReservationTimes(@RequestParam LocalDate date,
                                                                                      @RequestParam Long themeId) {
        List<ReservationTimeResponse> responses = reservationTimeService.getAvailableReservationTimes(date, themeId);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{reservationTimeId}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable Long reservationTimeId) {
        reservationTimeService.deleteReservationTime(reservationTimeId);
        return ResponseEntity.noContent().build();
    }
}
