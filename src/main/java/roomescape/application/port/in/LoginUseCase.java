package roomescape.application.port.in;

import roomescape.application.dto.UserCommand;
import roomescape.application.dto.UserResponse;

public interface LoginUseCase {

  boolean checkInvalidLogin(String principal, String credentials);

  String createToken(UserCommand userCommand);

  UserResponse findUser(String payload);

  UserResponse findUserByJwt(String jwt);
}
