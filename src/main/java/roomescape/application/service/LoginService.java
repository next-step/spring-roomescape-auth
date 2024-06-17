package roomescape.application.service;

import org.springframework.stereotype.Service;
import roomescape.application.dto.LoginCommand;
import roomescape.application.dto.MemberCommand;
import roomescape.application.dto.MemberResponse;
import roomescape.application.port.in.LoginUseCase;
import roomescape.application.port.out.MemberPort;
import roomescape.config.auth.provider.JwtTokenProvider;
import roomescape.domain.Member;
import roomescape.enums.Role;
import roomescape.exception.AuthenticationException;

@Service
public class LoginService implements LoginUseCase {

  private final JwtTokenProvider jwtTokenProvider;
  private final MemberPort memberPort;

  public LoginService(JwtTokenProvider jwtTokenProvider, MemberPort memberPort) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.memberPort = memberPort;
  }

  @Override
  public boolean checkInvalidLogin(LoginCommand loginCommand) {
    Member member = memberPort.findMemberByEmail(loginCommand.email()).orElseThrow(AuthenticationException::new);
    return !member.getPassword().equals(loginCommand.password());
  }

  @Override
  public String createToken(LoginCommand loginCommand) {
    if (checkInvalidLogin(loginCommand)) {
      throw new AuthenticationException();
    }

    return jwtTokenProvider.createToken(loginCommand.email(), Role.ADMIN.name());
  }

  @Override
  public MemberResponse findMember(String payload, Role role) {
      return new MemberResponse(payload, role);
  }

  @Override
  public MemberResponse findMemberByToken(String token) {
    if (!jwtTokenProvider.validateToken(token)) {
      throw new AuthenticationException();
    }

    String payload = jwtTokenProvider.getPayload(token);
    String role = jwtTokenProvider.getRole(token);
    return findMember(payload, Role.from(role));
  }
}
