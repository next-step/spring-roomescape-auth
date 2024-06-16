package roomescape.application.port.in;

import roomescape.application.dto.LoginCommand;
import roomescape.application.dto.MemberResponse;
import roomescape.enums.Role;

public interface LoginUseCase {

  boolean checkInvalidLogin(LoginCommand loginCommand);

  String createToken(LoginCommand loginCommand);

  MemberResponse findMember(String payload, Role role);

  MemberResponse findMemberByJwt(String jwt);
}
