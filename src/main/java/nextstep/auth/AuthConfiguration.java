package nextstep.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration(proxyBeanMethods = false)
public class AuthConfiguration {

  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }
}
