package nextstep.member;

import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.AuthService;
import nextstep.auth.TokenParseRequest;
import nextstep.auth.TokenParseResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

  private final AuthService authService;

  public AdminInterceptor(AuthService authService) {
    this.authService = authService;
  }


  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    var accessToken = extract(request, "Bearer ");
    var tokenParseResponse = parseToken(accessToken);
    var roles = tokenParseResponse.getRoles();
    return roles.contains(MemberRole.ADMIN);
  }


  private TokenParseResponse parseToken(String accessToken) {
    try {
      return authService.parseToken(new TokenParseRequest(accessToken));
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException();
    }
  }

  private String extract(HttpServletRequest request, String type) {
    Enumeration<String> headers = request.getHeaders(HttpHeaders.AUTHORIZATION);
    while (headers.hasMoreElements()) {
      String value = headers.nextElement();
      if (value.toLowerCase().startsWith(type.toLowerCase())) {
        return value.substring(type.length()).trim();
      }
    }

    return Strings.EMPTY;
  }
}
