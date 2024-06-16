package roomescape.domain;

import roomescape.enums.Role;

public class Member {

  private final String name;
  private final String email;
  private final String password;
  private final Role role;

  public Member(String name, String email, String password, Role role) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.role = role;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public Role getRole() {
    return role;
  }

  public static Member of(String name, String email, String password, Role role) {
    return new Member(name, email, password, role);
  }
}
