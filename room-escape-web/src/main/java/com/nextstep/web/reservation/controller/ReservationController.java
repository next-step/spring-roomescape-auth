package com.nextstep.web.reservation.controller;

import com.nextstep.web.common.LoginMember;
import com.nextstep.web.common.LoginMemberPrincipal;
import com.nextstep.web.reservation.app.ReservationCommandService;
import com.nextstep.web.reservation.app.ReservationQueryService;
import com.nextstep.web.reservation.dto.CreateReservationRequest;
import com.nextstep.web.reservation.dto.ReservationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationQueryService reservationQueryService;
    private final ReservationCommandService reservationCommandService;

    public ReservationController(ReservationQueryService reservationQueryService, ReservationCommandService reservationCommandService) {
        this.reservationQueryService = reservationQueryService;
        this.reservationCommandService = reservationCommandService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> read(@RequestParam String date) {
        return ResponseEntity.ok(reservationQueryService.findAllBy(date));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateReservationRequest request,
                                       @LoginMemberPrincipal LoginMember loginMember) {
        Long id = reservationCommandService.save(request, loginMember);
        return ResponseEntity.created(URI.create("/reservation/" + id)).build();
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> delete(@PathVariable Long id, @LoginMemberPrincipal LoginMember loginMember) {
        reservationCommandService.delete(id, loginMember);
        return ResponseEntity.noContent().build();
    }
}
