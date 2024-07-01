package roomescape.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;

public class TokenUtil {
  @Value("${jwt.secret}")
  private static String secretKey;

  public static String extractTokenFromCookie(Cookie[] cookies){
    for(Cookie cookie : cookies){
      if(cookie.getName().equals("token")){
        return cookie.getValue();
      }
    }
    throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
  }

  public static String getEmailFromToken(String token){
    return Jwts.parserBuilder()
      .setSigningKey(Keys.hmacShaKeyFor(getSecretKey().getBytes()))
      .build()
      .parseClaimsJws(token)
      .getBody()
      .getSubject();
  }

  public static String getSecretKey(){
    return secretKey;
  }
}
