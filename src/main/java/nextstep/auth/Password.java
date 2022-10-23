package nextstep.auth;

import java.util.Objects;

public record Password(Long id, String value) {

  public boolean isSame(String encryptPassword){
    return Objects.equals(encryptPassword, value);
  }
}
