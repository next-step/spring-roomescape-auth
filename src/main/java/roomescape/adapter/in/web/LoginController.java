package roomescape.adapter.in.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import roomescape.annotation.UserInfo;
import roomescape.application.dto.LoginCommand;
import roomescape.application.dto.MemberResponse;
import roomescape.application.port.in.LoginUseCase;

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
  @GetMapping("/check")
  public ResponseEntity<MemberResponse> checkInvalidLogin(@UserInfo MemberResponse memberResponse) {
    return ResponseEntity.ok(memberResponse);
  }

  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  @PostMapping("")
  public void login(@RequestBody LoginCommand loginCommand, HttpServletResponse response) {
    String jwt = loginUseCase.createToken(loginCommand);

    Cookie jwtCookie = new Cookie("jwt", jwt);
    jwtCookie.setHttpOnly(true);
    jwtCookie.setPath("/");
    jwtCookie.setMaxAge(30 * 60);
    response.addCookie(jwtCookie);
  }
}
