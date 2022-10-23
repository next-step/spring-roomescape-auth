package nextstep.member;

import java.util.Collections;
import nextstep.auth.AuthService;
import nextstep.auth.PasswordCheckRequest;
import nextstep.auth.TokenRequest;
import nextstep.auth.TokenResponse;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

  private final MemberService memberService;
  private final AuthService authService;

  public LoginService(MemberService memberService, AuthService authService) {
    this.memberService = memberService;
    this.authService = authService;
  }

  public TokenResponse loginWithPassword(LoginRequest loginRequest) {
    Member user = memberService.findByUsername(loginRequest.getUsername());
    PasswordCheckRequest challenge = new PasswordCheckRequest(user.getPasswordId(),
        loginRequest.getPassword());

    if (authService.checkPassword(challenge).success()) {
      return authService.createToken(new TokenRequest(user.getId(), user.getRoles()));
    }

    throw new IllegalArgumentException();
  }
}
