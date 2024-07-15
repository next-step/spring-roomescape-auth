package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.config.TokenPropertiesConfig;
import roomescape.entities.Member;
import roomescape.service.MemberService;

public class AuthInterceptor implements HandlerInterceptor {
  private final TokenPropertiesConfig tokenPropertiesConfig;
  private final MemberService memberService;

  public AuthInterceptor(TokenPropertiesConfig tokenPropertiesConfig, MemberService memberService) {
    this.tokenPropertiesConfig = tokenPropertiesConfig;
    this.memberService = memberService;
  }

  @Override
  public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler) throws Exception {

    String token = tokenPropertiesConfig.extractTokenFromCookie(request.getCookies());
    Member member = memberService.findMemberByEmail(tokenPropertiesConfig.getEmailFromToken(token));

    if (member == null || member.getRole() != "admin") {
      response.sendRedirect("/login");
      return false;
    }
    return true;
  }
}
