package roomescape.apply.reservationtime.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.apply.auth.application.annotation.NeedMemberRole;
import roomescape.apply.member.domain.MemberRoleName;
import roomescape.apply.reservationtime.application.ReservationTimeDeleter;
import roomescape.apply.reservationtime.application.ReservationTimeFinder;
import roomescape.apply.reservationtime.application.ReservationTimeSaver;
import roomescape.apply.reservationtime.ui.dto.AvailableReservationTimeResponse;
import roomescape.apply.reservationtime.ui.dto.ReservationTimeRequest;
import roomescape.apply.reservationtime.ui.dto.ReservationTimeResponse;

import java.util.List;

@RestController
@RequestMapping("/times")
public class TimeController {

    private final ReservationTimeFinder reservationTimeFinder;
    private final ReservationTimeSaver reservationTimeSaver;
    private final ReservationTimeDeleter reservationTimeDeleter;

    public TimeController(ReservationTimeFinder reservationTimeFinder, ReservationTimeSaver reservationTimeSaver,
                          ReservationTimeDeleter reservationTimeDeleter
    ) {
        this.reservationTimeFinder = reservationTimeFinder;
        this.reservationTimeSaver = reservationTimeSaver;
        this.reservationTimeDeleter = reservationTimeDeleter;
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> findAllTimes() {
        List<ReservationTimeResponse> responses = reservationTimeFinder.findAll();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    @NeedMemberRole(MemberRoleName.ADMIN)
    public ResponseEntity<ReservationTimeResponse> createTime(@RequestBody ReservationTimeRequest request) {
        ReservationTimeResponse response = reservationTimeSaver.saveReservationTimeBy(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    @NeedMemberRole(MemberRoleName.ADMIN)
    public ResponseEntity<Void> deleteTime(@PathVariable("id") long id) {
        reservationTimeDeleter.deleteReservationTimeBy(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<AvailableReservationTimeResponse>> getAvailableTimes(@RequestParam("date") String date,
                                                                                    @RequestParam("themeId") String themeId
    ) {
        var response = reservationTimeFinder.findAvailableTimesBy(date, themeId);
        return ResponseEntity.ok(response);
    }

}
