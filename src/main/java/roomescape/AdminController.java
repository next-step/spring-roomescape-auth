package roomescape;

import java.net.URI;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import roomescape.reservation.application.ReservationService;
import roomescape.reservation.dto.ReservationCreateRequest;
import roomescape.reservation.dto.ReservationResponse;

@Controller
public class AdminController {

    private final ReservationService reservationService;

    public AdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/")
    public String index() {
        return "admin/index";
    }

    @GetMapping("/admin/reservation")
    public String reservation() {
        return "admin/reservation";
    }

    @PostMapping("/admin/reservation")
    @ResponseBody
    public ResponseEntity<ReservationResponse> reservationPost(@RequestBody @Valid ReservationCreateRequest request) {
        ReservationResponse response = reservationService.save(request, request.memberId());
        return ResponseEntity.created(URI.create("/reservations/" + response.id())).body(response);
    }

    @GetMapping("/admin/time")
    public String time() {
        return "admin/time";
    }

    @GetMapping("/admin/theme")
    public String theme() {
        return "admin/theme";
    }
}
