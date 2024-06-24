package roomescape.apply.reservation.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.apply.auth.application.annotation.NeedMemberRole;
import roomescape.apply.member.domain.MemberRoleName;
import roomescape.apply.reservation.application.ReservationCanceler;
import roomescape.apply.reservation.application.ReservationFinder;
import roomescape.apply.reservation.application.ReservationRecorder;
import roomescape.apply.reservation.application.ReservationSearcher;
import roomescape.apply.reservation.ui.dto.ReservationAdminRequest;
import roomescape.apply.reservation.ui.dto.ReservationAdminResponse;
import roomescape.apply.reservation.ui.dto.ReservationSearchParams;

import java.util.List;

@RestController
@RequestMapping("/admin/reservations")
public class ReservationAdminController {

    private final ReservationRecorder reservationRecorder;
    private final ReservationFinder reservationFinder;
    private final ReservationCanceler reservationCanceler;
    private final ReservationSearcher reservationSearcher;

    public ReservationAdminController(ReservationRecorder reservationRecorder,
                                      ReservationFinder reservationFinder,
                                      ReservationCanceler reservationCanceler,
                                      ReservationSearcher reservationSearcher) {
        this.reservationRecorder = reservationRecorder;
        this.reservationFinder = reservationFinder;
        this.reservationCanceler = reservationCanceler;
        this.reservationSearcher = reservationSearcher;
    }

    @GetMapping
    public ResponseEntity<List<ReservationAdminResponse>> getReservations() {
        return ResponseEntity.ok(reservationFinder.findAllForAdmin());
    }

    @PostMapping
    @NeedMemberRole({MemberRoleName.ADMIN})
    public ResponseEntity<ReservationAdminResponse> adminAddReservation(@RequestBody ReservationAdminRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationRecorder.recordReservationBy(request));
    }

    @DeleteMapping("/{id}")
    @NeedMemberRole({MemberRoleName.ADMIN, MemberRoleName.GUEST})
    public ResponseEntity<Void> cancelReservation(@PathVariable("id") long id) {
        reservationCanceler.cancelReservation(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/search")
    @NeedMemberRole(MemberRoleName.ADMIN)
    public ResponseEntity<List<ReservationAdminResponse>> searchReservations(
            @RequestParam(value = "themeId", required = false) Long themeId,
            @RequestParam(value = "memberId", required = false) Long memberId,
            @RequestParam(value = "dateFrom", required = false) String dateFrom,
            @RequestParam(value = "dateTo", required = false) String dateTo
    ) {
        ReservationSearchParams searchParams = new ReservationSearchParams(themeId, memberId, dateFrom, dateTo);
        var responses = reservationSearcher.searchReservations(searchParams);
        return ResponseEntity.ok(responses);
    }

}
