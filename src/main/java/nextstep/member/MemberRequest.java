package nextstep.member;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record MemberRequest(String username, String password, String name, String phone, RoleType role) {

  public Member toEntity() {
    return new Member(username, password, name, phone, role);
  }
}
