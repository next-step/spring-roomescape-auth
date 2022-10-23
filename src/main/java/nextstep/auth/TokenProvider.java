package nextstep.auth;

import java.util.List;

public interface TokenProvider {

  String createToken(String principal, List<String> roles);

  String getPrincipal(String token);

  List<String> getRoles(String token);

  boolean validateToken(String token);
}
