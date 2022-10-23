package nextstep.member;

import nextstep.auth.AuthorizationExtractor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final AuthorizationExtractor authorizationExtractor;

    public MemberController(MemberService memberService, AuthorizationExtractor authorizationExtractor) {
        this.memberService = memberService;
        this.authorizationExtractor = authorizationExtractor;
    }

    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody MemberRequest memberRequest) {
        Long id = memberService.create(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + id)).build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> me(HttpServletRequest request) {
        Long userId = authorizationExtractor.extractClaims(request);
        Member member = memberService.findById(userId);
        MemberResponse response = MemberResponse.from(member);
        return ResponseEntity.ok(response);
    }
}
