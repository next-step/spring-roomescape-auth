package roomescape.application.service;

import org.springframework.stereotype.Service;
import roomescape.application.dto.UserCommand;
import roomescape.application.dto.UserResponse;
import roomescape.application.port.in.LoginUseCase;
import roomescape.auth.provider.JwtTokenProvider;
import roomescape.exception.AuthenticationException;

@Service
public class LoginService implements LoginUseCase {

  private static final String EMAIL = "joisfe@gmail.com";
  private static final String PASSWORD = "1234";

  private final JwtTokenProvider jwtTokenProvider;

  public LoginService(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  public boolean checkInvalidLogin(String principal, String credentials) {
    return !EMAIL.equals(principal) || !PASSWORD.equals(credentials);
  }

  @Override
  public String createToken(UserCommand userCommand) {
    if (checkInvalidLogin(userCommand.email(), userCommand.password())) {
      throw new AuthenticationException();
    }

    return jwtTokenProvider.createToken(userCommand.email());
  }

  @Override
  public UserResponse findUser(String payload) {
    return new UserResponse("누군가");
  }

  @Override
  public UserResponse findUserByJwt(String jwt) {
    String payload = jwtTokenProvider.getPayload(jwt);
    return findUser(payload);
  }
}
