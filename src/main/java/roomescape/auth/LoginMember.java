package roomescape.auth;

import lombok.Getter;

@Getter
public class LoginMember {

  private String email;

  private String token;

  public LoginMember(String email, String token) {
    this.email = email;
    this.token = token;
  }
}
