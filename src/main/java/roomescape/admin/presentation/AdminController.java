package roomescape.admin.presentation;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import roomescape.admin.dto.SearchRequest;
import roomescape.reservation.application.ReservationService;
import roomescape.reservation.dto.ReservationCreateRequest;
import roomescape.reservation.dto.ReservationResponse;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ReservationService reservationService;

    public AdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> create(@RequestBody @Valid ReservationCreateRequest request) {
        ReservationResponse response = reservationService.save(request, request.memberId());
        return ResponseEntity.created(URI.create("/reservations/" + response.id())).body(response);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> search(@ModelAttribute SearchRequest request) {
        List<ReservationResponse> responses = reservationService.search(request);
        return ResponseEntity.ok().body(responses);
    }
}
