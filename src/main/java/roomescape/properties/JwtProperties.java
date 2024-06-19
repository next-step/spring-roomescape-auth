package roomescape.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

  private String secretKey;
  private Long validityInMilliseconds;


  public String getSecretKey() {
    return secretKey;
  }

  public Long getValidityInMilliseconds() {
    return validityInMilliseconds;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  public void setValidityInMilliseconds(Long validityInMilliseconds) {
    this.validityInMilliseconds = validityInMilliseconds;
  }
}
