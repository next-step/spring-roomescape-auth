package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.LoginMemberArgumentResolver;
import roomescape.service.MemberService;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  private MemberService memberService;
  private TokenPropertiesConfig tokenPropertiesConfig;

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new LoginMemberArgumentResolver(memberService, tokenPropertiesConfig));
  }
}
