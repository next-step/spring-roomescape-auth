package roomescape.util;

import jakarta.servlet.http.Cookie;

public class TokenUtil {

  public static String extractTokenFromCookie(Cookie[] cookies){
    for(Cookie cookie : cookies){
      if(cookie.getName().equals("token")){
        return cookie.getValue();
      }
    }
    throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
  }
}
