package roomescape.application.port.in;

import roomescape.application.dto.LoginCommand;
import roomescape.application.dto.MemberCommand;
import roomescape.application.dto.MemberResponse;

public interface LoginUseCase {

  boolean checkInvalidLogin(LoginCommand loginCommand);

  String createToken(LoginCommand loginCommand);

  MemberResponse findMember(String payload);

  MemberResponse findUserByJwt(String jwt);
}
