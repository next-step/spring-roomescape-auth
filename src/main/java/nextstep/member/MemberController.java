package nextstep.member;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import nextstep.auth.JwtTokenProvider;
import nextstep.auth.MemberAuthentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
  public ResponseEntity me(@MemberAuthentication Member member) {
    Member findMember = memberService.findById(Long.valueOf(member.getId()));
    return ResponseEntity.ok(findMember);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handle(RuntimeException exception) {
    return ResponseEntity.badRequest().body(exception.getMessage());
  }
}
