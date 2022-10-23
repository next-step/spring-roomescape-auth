package nextstep.member;

import nextstep.auth.AuthService;
import nextstep.auth.TokenParseRequest;
import nextstep.auth.TokenParseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/members")
public class MemberController {

  private final MemberService memberService;
  private final AuthService authService;

  public MemberController(MemberService memberService, AuthService authService) {
    this.memberService = memberService;
    this.authService = authService;
  }

  @PostMapping
  public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
    Long id = memberService.create(memberRequest);
    return ResponseEntity.created(URI.create("/members/" + id)).build();
  }

  @GetMapping("/me")
  public ResponseEntity<Member> me(@RequestBody MemberMeRequest request) {
    TokenParseResponse tokenParseResponse = authService.parseToken(new TokenParseRequest(request.accessToken()));
    Member member = memberService.findById(tokenParseResponse.getSubject());
    return ResponseEntity.ok(member);
  }
}
