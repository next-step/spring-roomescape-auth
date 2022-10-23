package nextstep.member;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import nextstep.auth.AuthService;
import nextstep.auth.TokenParseRequest;
import nextstep.auth.TokenParseResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginAuthenticationArgumentResolver implements HandlerMethodArgumentResolver {

  private final AuthService authService;
  private final MemberService memberService;

  public LoginAuthenticationArgumentResolver(AuthService authService, MemberService memberService) {
    this.authService = authService;
    this.memberService = memberService;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    var parameterType = parameter.getParameterType();
    return LoginAuthentication.class.isAssignableFrom(parameterType);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

    String accessToken = extract(webRequest.getNativeRequest(HttpServletRequest.class), "Bearer ");
    TokenParseResponse tokenParseResponse = parseToken(accessToken);
    Member loginMember = memberService.findById(tokenParseResponse.getSubject());
    return new LoginAuthentication(loginMember);
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
