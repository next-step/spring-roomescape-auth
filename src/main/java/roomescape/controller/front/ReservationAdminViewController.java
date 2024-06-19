package roomescape.controller.front;


import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import roomescape.domain.Member;
import roomescape.dto.request.ReservationAdminRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.MemberService;
import roomescape.service.ReservationService;

@RequestMapping("/admin")
@Controller
public class ReservationAdminViewController {

    private final ReservationService reservationService;
    private final MemberService memberService;

    public ReservationAdminViewController(ReservationService reservationService, MemberService memberService) {
        this.reservationService = reservationService;
        this.memberService = memberService;
    }

    @GetMapping
    public String index() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String reservation() {
        return "admin/reservation";
    }

    @GetMapping("/time")
    public String time() {
        return "admin/time";
    }

    @GetMapping("/theme")
    public String theme() {
        return "admin/theme";
    }

    @PostMapping("/reservations")
    @ResponseBody
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationAdminRequest request) {
        Member member = memberService.findById(request.getMemberId());

        ReservationResponse reservation = reservationService.createAdminReservation(request, member);
        return ResponseEntity
                .created(URI.create("/reservations/" + reservation.getId()))
                .body(reservation);
    }
}
