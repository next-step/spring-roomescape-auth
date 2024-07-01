package roomescape.reservation.ui;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.application.FindMemberService;
import roomescape.reservation.application.ReservationService;
import roomescape.reservation.ui.dto.AdminReservationRequest;
import roomescape.reservation.ui.dto.ReservationRequest;
import roomescape.reservation.ui.dto.ReservationResponse;

@RestController
@RequestMapping("admin/reservations")
public class AdminReservationController {
    private final ReservationService reservationService;
    private final FindMemberService findMemberService;


    public AdminReservationController(ReservationService reservationService, FindMemberService findMemberService) {
        this.reservationService = reservationService;
        this.findMemberService = findMemberService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@RequestBody @Valid AdminReservationRequest adminReservationRequest) {
        Long memberId = findMemberService.findOne(adminReservationRequest.memberId()).id();
        ReservationRequest request = ReservationRequest.fromAdminRequest(memberId, adminReservationRequest);
        ReservationResponse reservationResponse = reservationService.make(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationResponse);
    }
}
