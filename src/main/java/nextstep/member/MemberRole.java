package nextstep.member;

import java.util.List;

public enum MemberRole {
  ADMIN,
  USER;

  public static List<MemberRole> valueOf(List<String> roles) {
    return roles.stream()
        .map(MemberRole::valueOf)
        .toList();
  }
}
