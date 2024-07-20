package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.argumentresolver.LoginUser;
import roomescape.domain.Role;
import roomescape.domain.User;
import roomescape.dto.reservation.ReservationsResponse;
import roomescape.dto.reservation.create.ReservationCreateRequest;
import roomescape.dto.reservation.create.ReservationCreateResponse;
import roomescape.exception.ErrorCode;
import roomescape.exception.custom.AuthorizationException;
import roomescape.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationsResponse>> findReservations() {
        List<ReservationsResponse> list = reservationService.findReservations();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationCreateResponse> create(@Valid @RequestBody ReservationCreateRequest dto,
                                                            @LoginUser User user) {
        dto.addUserName(user.getName());
        ReservationCreateResponse reservation = reservationService.createReservation(dto);
        return ResponseEntity.ok().body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationCreateResponse> createAdminReservation(
            @Valid @RequestBody ReservationCreateRequest requestDto,
            @LoginUser User user){

        if(user.getRole() != Role.ADMIN){
            throw new AuthorizationException(ErrorCode.UNAUTHORIZED_ADMIN, "관리자 권한이 필요합니다.");
        }

        ReservationCreateResponse reservation = reservationService.createReservation(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }
}
