package nextstep.play;

import java.net.URI;
import nextstep.auth.LoginMember;
import nextstep.auth.LoginMemberPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/plays")
public class PlayController {

    private final PlayService playService;

    public PlayController(PlayService playService) {
        this.playService = playService;
    }

    @PostMapping
    public ResponseEntity playReservation(
        @LoginMemberPrincipal LoginMember loginMember,
        @RequestBody PlayRequest request
    ) {
        Long id = playService.play(request, loginMember.getId());
        return ResponseEntity.created(URI.create("/plays/" + id)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> playReservation(
        @LoginMemberPrincipal LoginMember loginMember,
        @PathVariable Long id
    ) {
        playService.delete(id, loginMember.getId());
        return ResponseEntity.ok().build();
    }
}
