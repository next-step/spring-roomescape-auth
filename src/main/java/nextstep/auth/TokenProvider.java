package nextstep.auth;

import java.util.List;
import nextstep.member.MemberRole;

public interface TokenProvider {

  String createToken(String principal, List<MemberRole> roles);

  String getPrincipal(String token);

  List<String> getRoles(String token);

  boolean validateToken(String token);
}
