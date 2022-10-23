package nextstep.member;

import java.net.URI;
import nextstep.auth.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

  private final LoginService loginService;

  public LoginController(LoginService loginService) {
    this.loginService = loginService;
  }

  @PostMapping("/token")
  public ResponseEntity<TokenResponse> createToken(@RequestBody LoginRequest loginRequest) {
    return ResponseEntity.ok(loginService.loginWithPassword(loginRequest));
  }

}