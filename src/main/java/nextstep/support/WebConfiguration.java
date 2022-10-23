package nextstep.support;

import java.util.List;
import nextstep.member.LoginAuthenticationArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
public class WebConfiguration implements WebMvcConfigurer {

  LoginAuthenticationArgumentResolver authenticationArgumentResolver;

  public WebConfiguration(LoginAuthenticationArgumentResolver authenticationArgumentResolver) {
    this.authenticationArgumentResolver = authenticationArgumentResolver;
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**").allowedMethods("*").allowedOriginPatterns("*");
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
      resolvers.add(authenticationArgumentResolver);
  }
}
