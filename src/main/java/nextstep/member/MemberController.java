package nextstep.member;

import java.net.URI;
import java.util.List;
import nextstep.auth.LoginMember;
import nextstep.auth.LoginMemberPrincipal;
import nextstep.play.Play;
import nextstep.play.PlayResponse;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {

    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        Long id = memberService.create(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + id)).build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> me(@LoginMemberPrincipal LoginMember loginMember) {
        Member member = memberService.findById(loginMember.getId());
        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @GetMapping("/{id}/reservations")
    public ResponseEntity<List<ReservationResponse>> getReservations(
        @PathVariable Long id,
        @LoginMemberPrincipal LoginMember loginMember
    ) {
        List<Reservation> reservations = memberService.findAllReservationsByMemberId(
            id, loginMember.getId()
        );
        return ResponseEntity.ok(ReservationResponse.of(reservations));
    }

    @GetMapping("/{id}/plays")
    public ResponseEntity<List<PlayResponse>> getPlays(
        @PathVariable Long id,
        @LoginMemberPrincipal LoginMember loginMember
    ) {
        List<Play> plays = memberService.findAllPlaysByMemberId(id, loginMember.getId());
        return ResponseEntity.ok(PlayResponse.of(plays));
    }
}
