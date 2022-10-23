package nextstep.reservation.presentation;

import nextstep.auth.resolver.AuthenticationPrincipal;
import nextstep.auth.resolver.LoginUser;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationService;
import nextstep.reservation.presentation.dto.ReservationRequest;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> createReservation(@AuthenticationPrincipal LoginUser loginUser, @RequestBody ReservationRequest reservationRequest) {
        return loginUser.act(ResponseEntity.class)
            .ifAnonymous(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build())
            .ifUser(uuid -> {
                Long id = reservationService.create(reservationRequest, uuid);
                return ResponseEntity.created(URI.create("/reservations/" + id)).build();
            })
            .getResult();

    }

    @GetMapping
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteReservation(@AuthenticationPrincipal LoginUser loginUser, @PathVariable Long id) {
        return loginUser.act(ResponseEntity.class)
            .ifAnonymous(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build())
            .ifUser(uuid -> {
                try {
                    reservationService.deleteById(id, uuid);
                    return ResponseEntity.noContent().build();
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                }
            })
            .getResult();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity onException(Exception e) {
        return ResponseEntity.badRequest().build();
    }
}
