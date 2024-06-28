package roomescape.reservation.ui;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.application.MemberService;
import roomescape.reservation.application.ReservationService;
import roomescape.reservation.ui.dto.AdminReservationRequest;
import roomescape.reservation.ui.dto.ReservationRequest;
import roomescape.reservation.ui.dto.ReservationResponse;

@RestController
@RequestMapping("admin/reservations")
public class AdminReservationController {
    private final ReservationService reservationService;
    private final MemberService memberService;


    public AdminReservationController(ReservationService reservationService, MemberService memberService) {
        this.reservationService = reservationService;
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@RequestBody @Valid AdminReservationRequest adminReservationRequest) {
        String memberName = memberService.findOne(adminReservationRequest.memberId()).name();
        ReservationRequest request = ReservationRequest.fromAdminRequest(memberName, adminReservationRequest);
        Long reservationId = reservationService.make(request);
        ReservationResponse reservationResponse = reservationService.findOne(reservationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationResponse);
    }
}
