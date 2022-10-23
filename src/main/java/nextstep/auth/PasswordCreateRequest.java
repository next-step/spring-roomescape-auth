package nextstep.auth;

import java.util.function.Function;

public record PasswordCreateRequest(String rawPassword) {

  public Password toEntity(Function<String, String> encoder){
    return new Password(null, encoder.apply(rawPassword));
  }
}
