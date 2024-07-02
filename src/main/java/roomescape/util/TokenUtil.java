package roomescape.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import roomescape.config.TokenPropertiesConfig;

public class TokenUtil {

  public static String extractTokenFromCookie(Cookie[] cookies){
    for(Cookie cookie : cookies){
      if(cookie.getName().equals("token")){
        return cookie.getValue();
      }
    }
    throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
  }

  public static String getEmailFromToken(String token, TokenPropertiesConfig tokenPropertiesConfig){
    return Jwts.parserBuilder()
      .setSigningKey(Keys.hmacShaKeyFor(tokenPropertiesConfig.getSecretKey().getBytes()))
      .build()
      .parseClaimsJws(token)
      .getBody()
      .getSubject();
  }
}
