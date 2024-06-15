package roomescape.apply.reservation.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.apply.auth.application.annotation.NeedMemberRole;
import roomescape.apply.auth.ui.dto.LoginMember;
import roomescape.apply.member.domain.MemberRoleName;
import roomescape.apply.reservation.application.ReservationCanceler;
import roomescape.apply.reservation.application.ReservationFinder;
import roomescape.apply.reservation.application.ReservationRecorder;
import roomescape.apply.reservation.ui.dto.ReservationRequest;
import roomescape.apply.reservation.ui.dto.ReservationResponse;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationRecorder reservationRecorder;
    private final ReservationFinder reservationFinder;
    private final ReservationCanceler reservationCanceler;

    public ReservationController(ReservationRecorder reservationRecorder, ReservationFinder reservationFinder,
                                 ReservationCanceler reservationCanceler) {
        this.reservationRecorder = reservationRecorder;
        this.reservationFinder = reservationFinder;
        this.reservationCanceler = reservationCanceler;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        return ResponseEntity.ok(reservationFinder.findAll());
    }

    @PostMapping
    @NeedMemberRole({MemberRoleName.ADMIN, MemberRoleName.GUEST})
    public ResponseEntity<ReservationResponse> addReservation(@RequestBody ReservationRequest request,
                                                              LoginMember loginMember
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationRecorder.recordReservationBy(request, loginMember));
    }

    @DeleteMapping("/{id}")
    @NeedMemberRole({MemberRoleName.ADMIN, MemberRoleName.GUEST})
    public ResponseEntity<Void> cancelReservation(@PathVariable("id") long id) {
        reservationCanceler.cancelReservation(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
