package nextstep.auth;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import nextstep.member.MemberService;
import nextstep.member.RoleType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AdminInterceptor implements HandlerInterceptor {

  private final MemberService memberService;
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    var token = request.getHeader("authorization");
    if (Objects.isNull(token) || !jwtTokenProvider.validateToken(token)) {
      throw new AuthenticationException("인증되지 않은 토큰입니다.");
    }
    var roles = jwtTokenProvider.getRoles(token);
    if (roles.contains(RoleType.ADMIN.name())) {
      return true;
    }
    throw new AuthenticationException("관리자만 접근가능한 URI입니다.");
  }
}
