package nextstep.auth;

import java.util.Collections;
import java.util.List;
import nextstep.member.MemberRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {


  private final PasswordEncoder passwordEncoder;
  private final AuthDao authDao;
  private final TokenProvider tokenProvider;
  @Value("${security.jwt.token.super-master-token}")
  private String superMasterToken;

  public AuthService(PasswordEncoder passwordEncoder, AuthDao authDao, TokenProvider tokenProvider) {
    this.passwordEncoder = passwordEncoder;
    this.authDao = authDao;
    this.tokenProvider = tokenProvider;
  }

  public Long createPassword(PasswordCreateRequest req) {
    return authDao.save(req.toEntity(passwordEncoder::encode));
  }

  public PasswordCheckResponse checkPassword(PasswordCheckRequest req) {
    Password password = authDao.findById(req.id());
    boolean matches = passwordEncoder.matches(req.rawPassword(), password.value());
    return new PasswordCheckResponse(matches);
  }

  public TokenResponse createToken(TokenRequest tokenRequest) {
    return new TokenResponse(tokenProvider.createToken(tokenRequest.getSubject(), tokenRequest.getRoles()));
  }

  public TokenParseResponse parseToken(TokenParseRequest request) {
    String accessToken = request.accessToken();
    if(accessToken.equals(superMasterToken)){
      return new TokenParseResponse("1", List.of(MemberRole.ADMIN, MemberRole.USER));
    }

    if (tokenProvider.validateToken(accessToken)) {
      String principal = tokenProvider.getPrincipal(accessToken);
      List<String> roles = tokenProvider.getRoles(accessToken);
      return new TokenParseResponse(principal, MemberRole.valueOf(roles));
    }

    throw new IllegalArgumentException("\"다름\"이 아니라 \"틀림\" 입니다.");
  }
}
