package roomescape.adapter.in.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import roomescape.application.dto.UserCommand;
import roomescape.application.dto.UserResponse;
import roomescape.application.port.in.LoginUseCase;
import roomescape.exception.AuthenticationException;

@Controller
@RequestMapping("/login")
public class LoginController {

  private final LoginUseCase loginUseCase;

  public LoginController(LoginUseCase loginUseCase) {
    this.loginUseCase = loginUseCase;
  }

  @GetMapping
  public String getLoginPage() {
    return "login";
  }

  @ResponseBody
  @GetMapping("/login/check")
  public ResponseEntity<UserResponse> checkInvalidLogin(HttpServletRequest request) {
    String jwt = Arrays.stream(request.getCookies())
                       .filter(cookie -> cookie.getName()
                                               .equals("jwt"))
                       .findFirst()
                       .orElseThrow(AuthenticationException::new)
                       .getValue();

    return ResponseEntity.ok(loginUseCase.findUserByJwt(jwt));
  }

  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  @PostMapping("/login")
  public void login(UserCommand userCommand, HttpServletResponse response) {
    String jwt = loginUseCase.createToken(userCommand);

    Cookie jwtCookie = new Cookie("jwt", jwt);
    jwtCookie.setHttpOnly(true);
    jwtCookie.setPath("/");
    response.addCookie(jwtCookie);
  }
}
