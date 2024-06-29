package roomescape.reservationtime.ui;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservationtime.ui.dto.ReservationTimeRequest;
import roomescape.reservationtime.ui.dto.ReservationTimeResponse;
import roomescape.reservationtime.application.ReservationTimeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/times")
public class ReservationTimeController {
    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> readAll() {
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(reservationTimes);
    }

    @GetMapping("{id}")
    public ResponseEntity<ReservationTimeResponse> readOne(@PathVariable Long id) {
        ReservationTimeResponse reservationTime = reservationTimeService.findOne(id);
        return ResponseEntity.status(HttpStatus.OK).body(reservationTime);
    }

    @GetMapping("/available")
    public ResponseEntity<List<ReservationTimeResponse>> readAvailable(@RequestParam String date, @RequestParam Long themeId) {
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.findMatchWith(date, themeId);
        return ResponseEntity.status(HttpStatus.OK).body(reservationTimes);
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> create(@RequestBody @Valid ReservationTimeRequest request) {
        long id = reservationTimeService.add(request);
        ReservationTimeResponse reservationTime = reservationTimeService.findOne(id);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/times/" + reservationTime.getId()))
                .body(reservationTime);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        reservationTimeService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
