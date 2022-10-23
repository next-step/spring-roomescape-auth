package nextstep.auth;

import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final PasswordEncoder passwordEncoder;
  private final AuthDao authDao;
  private final TokenProvider tokenProvider;

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
    if (tokenProvider.validateToken(accessToken)) {
      String principal = tokenProvider.getPrincipal(accessToken);
      List<String> roles = tokenProvider.getRoles(accessToken);
      return new TokenParseResponse(principal, roles);
    }

    throw new IllegalArgumentException("\"다름\"이 아니라 \"틀림\" 입니다.");
  }
}
