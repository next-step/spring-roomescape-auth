package nextstep.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final PasswordEncoder passwordEncoder;
  private final AuthDao authDao;

  public AuthService(PasswordEncoder passwordEncoder, AuthDao authDao) {
    this.passwordEncoder = passwordEncoder;
    this.authDao = authDao;
  }

  public Long createPassword(PasswordCreateRequest req) {
    return authDao.save(req.toEntity(passwordEncoder::encode));
  }

  public boolean checkPassword(PasswordCheckRequest req) {
    String encrypted = passwordEncoder.encode(req.rawPassword());
    Password password = authDao.findById(req.id());
    return password.isSame(encrypted);
  }

}
