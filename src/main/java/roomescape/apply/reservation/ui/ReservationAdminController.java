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
import roomescape.apply.reservation.ui.dto.ReservationAdminRequest;
import roomescape.apply.reservation.ui.dto.ReservationAdminResponse;

import java.util.List;

@RestController
@RequestMapping("/admin/reservations")
public class ReservationAdminController {

    private final ReservationRecorder reservationRecorder;
    private final ReservationFinder reservationFinder;
    private final ReservationCanceler reservationCanceler;

    public ReservationAdminController(ReservationRecorder reservationRecorder,
                                      ReservationFinder reservationFinder,
                                      ReservationCanceler reservationCanceler) {
        this.reservationRecorder = reservationRecorder;
        this.reservationFinder = reservationFinder;
        this.reservationCanceler = reservationCanceler;
    }

    @GetMapping
    public ResponseEntity<List<ReservationAdminResponse>> getReservations(LoginMember loginMember) {
        return ResponseEntity.ok(reservationFinder.findAllForAdmin(loginMember));
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

}
