package roomescape.entities;

import lombok.Builder;
import lombok.Getter;
import roomescape.enums.Role;

@Getter
@Builder
public class Member {
  private String name;

  private String email;

  private String password;

  private String role;
}
