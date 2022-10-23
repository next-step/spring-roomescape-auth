package nextstep.support;

import java.util.List;
import nextstep.member.AdminInterceptor;
import nextstep.member.LoginAuthenticationArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
public class WebConfiguration implements WebMvcConfigurer {

  private final LoginAuthenticationArgumentResolver authenticationArgumentResolver;
  private final AdminInterceptor adminInterceptor;

  public WebConfiguration(LoginAuthenticationArgumentResolver authenticationArgumentResolver,
      AdminInterceptor adminInterceptor) {
    this.authenticationArgumentResolver = authenticationArgumentResolver;
    this.adminInterceptor = adminInterceptor;
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**").allowedMethods("*").allowedOriginPatterns("*");
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(adminInterceptor).addPathPatterns("/admin/**");
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
      resolvers.add(authenticationArgumentResolver);
  }
}
