package roomescape.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/login")
public class LoginController {

  @GetMapping
  public String login() {
    return "login";
  }

  @PostMapping
  public ResponseEntity<String> login(String email, String password) {
    return ResponseEntity.ok().body("Login successful");
  }
}
