package nextstep.application.controller.reservation;

import java.net.URI;
import nextstep.application.controller.auth.Auth;
import nextstep.application.controller.auth.LoginMember;
import nextstep.application.dto.reservation.ReservationRequest;
import nextstep.application.service.reservation.ReservationCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationCommandService reservationCommandService;

    public ReservationController(
        ReservationCommandService reservationCommandService
    ) {
        this.reservationCommandService = reservationCommandService;
    }

    @PostMapping
    public ResponseEntity<Void> make(
        @Auth LoginMember loginMember,
        @RequestBody ReservationRequest request
    ) {
        Long reservationId = reservationCommandService.make(request, memberId(loginMember));
        return ResponseEntity.created(URI.create("/reservations/" + reservationId)).build();
    }

    private Long memberId(LoginMember loginMember) {
        return Long.valueOf(loginMember.getPrincipal());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> cancel(@Auth LoginMember loginMember, @PathVariable Long id) {
        reservationCommandService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}
