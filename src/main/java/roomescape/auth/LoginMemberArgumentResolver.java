package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.config.TokenPropertiesConfig;
import roomescape.service.MemberService;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
  private MemberService memberService;
  private TokenPropertiesConfig tokenProperties;

  public LoginMemberArgumentResolver(MemberService memberService, TokenPropertiesConfig tokenPropertiesConfig) {
    this.memberService = memberService;
    this.tokenProperties = tokenPropertiesConfig;
  }

  @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

  @Override
  public Object resolveArgument(
    MethodParameter parameter, ModelAndViewContainer mavContainer,
    NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();

    String token = tokenProperties.extractTokenFromCookie(httpServletRequest.getCookies());
    String email = tokenProperties.getEmailFromToken(token);

    return new LoginMember(email, token);
  }
}
