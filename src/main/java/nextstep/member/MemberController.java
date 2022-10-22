package nextstep.member;

import java.net.URI;
import nextstep.auth.AuthenticationException;
import nextstep.jwt.JwtTokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

    private MemberService memberService;
    private JwtTokenProvider jwtTokenProvider;

    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        Long id = memberService.create(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + id)).build();
    }

    @GetMapping("/me")
    public ResponseEntity<Member> me(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {
        if (bearerToken == null || !bearerToken.contains("Bearer")) {
            throw new AuthenticationException();
        }
        String token = bearerToken.replace("Bearer ", "").trim();
        String username = jwtTokenProvider.getPrincipal(token);
        return ResponseEntity.ok(
            memberService.findByUsername(username)
        );
    }
}
