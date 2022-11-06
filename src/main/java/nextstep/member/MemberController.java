package nextstep.member;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import nextstep.auth.AuthenticationException;
import nextstep.auth.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

  private final MemberService memberService;
  private final JwtTokenProvider tokenProvider;


  @PostMapping
  public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
    Long id = memberService.create(memberRequest);
    return ResponseEntity.created(URI.create("/members/" + id)).build();
  }

  @GetMapping("/me")
  public ResponseEntity me(@RequestHeader String authorization) {
    if (!tokenProvider.validateToken(authorization)) {
      throw new AuthenticationException("인증되지 않은 토큰입니다.");
    }
    var principal = tokenProvider.getPrincipal(authorization);
    Member member = memberService.findById(Long.valueOf(principal));
    return ResponseEntity.ok(member);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handle(RuntimeException exception) {
    return ResponseEntity.badRequest().body(exception.getMessage());
  }
}
