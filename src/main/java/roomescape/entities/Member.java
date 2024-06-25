package roomescape.entities;

import lombok.Builder;
@Builder
public class Member {
  private String name;

  private String email;

  private String password;
}
