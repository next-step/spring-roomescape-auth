package roomescape.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;


@Setter
@Getter
@Validated
@ConfigurationProperties(prefix = "jwt")
@Configuration
public class TokenPropertiesConfig {

    @NotBlank
    private String secretKey;

    public String getEmailFromToken(String token){
        return Jwts.parserBuilder()
          .setSigningKey(Keys.hmacShaKeyFor(this.secretKey.getBytes()))
          .build()
          .parseClaimsJws(token)
          .getBody()
          .getSubject();
    }

    public String extractTokenFromCookie(Cookie[] cookies){
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("token")){
                return cookie.getValue();
            }
        }
        throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
    }
}
