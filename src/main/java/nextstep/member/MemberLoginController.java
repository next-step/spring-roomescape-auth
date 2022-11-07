package nextstep.member;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.auth.JwtTokenProvider;
import nextstep.auth.TokenRequest;
import nextstep.auth.TokenResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberLoginController {

  private final JwtTokenProvider tokenProvider;
  private final MemberService memberService;

  @PostMapping("/login/token")
  public TokenResponse login(@RequestBody TokenRequest req) {
    var member = memberService.getByUserNameAndPassword(req.getUsername(), req.getPassword());
    var token = tokenProvider.createToken(String.valueOf(member.getId()), List.of(member.getRole().name()));
    return new TokenResponse(token);
  }
}
