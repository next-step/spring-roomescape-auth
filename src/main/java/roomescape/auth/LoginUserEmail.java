package roomescape.auth;

import lombok.Getter;

@Getter
public class LoginUserEmail {

  private String email;

  private String token;

  public LoginUserEmail(String email, String token) {
    this.email = email;
    this.token = token;
  }
}
