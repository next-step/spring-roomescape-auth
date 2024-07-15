package roomescape.auth;

import lombok.Getter;
import roomescape.enums.Role;

@Getter
public class LoginMember {

  private String email;

  private String token;

  private Role role;

  public LoginMember(String email, String token, Role role) {
    this.email = email;
    this.token = token;
    this.role = role;
  }
}
