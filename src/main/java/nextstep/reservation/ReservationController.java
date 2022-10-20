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
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<Void> createReservation(@LoginMemberPrincipal LoginMember loginMember, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.create(loginMember.getId(), reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@LoginMemberPrincipal LoginMember loginMember, @PathVariable Long id) {
        reservationService.deleteById(loginMember.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
