package roomescape.ui;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.config.TokenPropertiesConfig;
import roomescape.service.MemberService;
import roomescape.ui.data.LoginCheckResponse;
import roomescape.ui.data.LoginRequest;
import roomescape.ui.data.SignupRequest;

import java.util.Date;

@RequiredArgsConstructor
@PropertySource("classpath:application.yml")
@Controller
@RequestMapping
public class SignController {

  private final MemberService memberService;
  private final TokenPropertiesConfig tokenPropertiesConfig;

  @GetMapping("/login")
  public String login() {
    return "login";
  }

  @PostMapping("/login")
  public ResponseEntity<Cookie> login(
    @RequestBody LoginRequest loginRequest, HttpServletResponse response) {

    String accessToken = Jwts.builder()
      .setSubject(loginRequest.getEmail())
      .claim("email", loginRequest.getEmail())
      .signWith(Keys.hmacShaKeyFor(tokenPropertiesConfig.getSecretKey().getBytes()))
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간
      .compact();
    Cookie cookie = new Cookie("token", accessToken);
    response.addCookie(cookie);

    return ResponseEntity.ok().body(cookie);
  }

  @GetMapping("/login/check")
  public ResponseEntity<LoginCheckResponse> check(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    String token = tokenPropertiesConfig.extractTokenFromCookie(cookies);
    String email = Jwts.parserBuilder()
      .setSigningKey(Keys.hmacShaKeyFor(tokenPropertiesConfig.getSecretKey().getBytes()))
      .build()
      .parseClaimsJws(token)
      .getBody()
      .getSubject();

    return ResponseEntity.ok().body(new LoginCheckResponse(email));
  }

  @GetMapping("/signup")
  public String signup(){
    return "signup";
  }

  @PostMapping("/signup")
  public ResponseEntity signup(SignupRequest signupRequest){
    memberService.save(signupRequest);
    return ResponseEntity.ok().build();
  }
}
