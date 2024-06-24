package roomescape.ui;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.ui.data.LoginRequest;

import java.security.Key;
import java.util.Date;

@PropertySource("classpath:application.yml")
@Controller
@RequestMapping("/login")
public class LoginController {

  @Value("${jwt.secret}")
  private String token;

  @GetMapping
  public String login() {
    return "login";
  }

  @PostMapping
  public ResponseEntity<Cookie> login(
    @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    String accessToken = Jwts.builder()
      .setSubject(loginRequest.getEmail())
      .signWith(key)
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
      .compact();
    Cookie cookie = new Cookie("token", accessToken);
    response.addCookie(cookie);

    return ResponseEntity.ok().body(cookie);
  }
}
