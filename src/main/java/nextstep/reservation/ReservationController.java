package nextstep.reservation;

import nextstep.auth.LoginMember;
import nextstep.auth.LoginMemberPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    public final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity createReservation(
        @LoginMemberPrincipal LoginMember loginMember,
        @RequestBody ReservationRequest request
    ) {
        Long id = reservationService.create(request, loginMember.getId());
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(ReservationResponse.of(results));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteReservation(
        @PathVariable Long id,
        @LoginMemberPrincipal LoginMember loginMember
    ) {
        reservationService.deleteById(id, loginMember.getId());

        return ResponseEntity.noContent().build();
    }
}
