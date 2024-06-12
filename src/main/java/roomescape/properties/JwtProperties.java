package roomescape.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

  private String secretKey;
  private String validityInMilliseconds;

  public String getSecretKey() {
    return secretKey;
  }

  public String getValidityInMilliseconds() {
    return validityInMilliseconds;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  public void setValidityInMilliseconds(String validityInMilliseconds) {
    this.validityInMilliseconds = validityInMilliseconds;
  }
}
