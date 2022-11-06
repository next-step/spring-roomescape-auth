package nextstep.auth;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.member.MemberService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class MemberAuthenticationArgumentResolver implements HandlerMethodArgumentResolver {

  private final JwtTokenProvider jwtTokenProvider;
  private final MemberService memberService;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(MemberAuthentication.class);
  }

  @Override
  public Member resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    var request = webRequest.getNativeRequest(HttpServletRequest.class);
    var token = request.getHeader("authorization");

    if (Objects.isNull(token) || !jwtTokenProvider.validateToken(token)) {
      throw new AuthenticationException("인증되지 않은 토큰입니다.");
    }
    var memberId = jwtTokenProvider.getPrincipal(token);
    return memberService.findById(Long.valueOf(memberId));
  }
}
