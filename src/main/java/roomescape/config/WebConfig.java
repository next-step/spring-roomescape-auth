package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.LoginMemberArgumentResolver;
import roomescape.auth.AuthInterceptor;
import roomescape.service.MemberService;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  private final MemberService memberService;
  private final TokenPropertiesConfig tokenPropertiesConfig;

  public WebConfig(MemberService memberService, TokenPropertiesConfig tokenPropertiesConfig) {
    this.memberService = memberService;
    this.tokenPropertiesConfig = tokenPropertiesConfig;
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new LoginMemberArgumentResolver(memberService, tokenPropertiesConfig));
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new AuthInterceptor(tokenPropertiesConfig, memberService))
            .addPathPatterns("/admin/**");
  }
}
