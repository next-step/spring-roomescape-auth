package roomescape.config;

import java.util.List;

import roomescape.service.AuthService;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final AuthService authService;

	private final LoginMemberArgumentResolver loginMemberArgumentResolver;

	WebConfig(LoginMemberArgumentResolver loginMemberArgumentResolver, AuthService authService) {
		this.loginMemberArgumentResolver = loginMemberArgumentResolver;
		this.authService = authService;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(this.loginMemberArgumentResolver);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new RoleInterceptor(this.authService)).addPathPatterns("/admin/**");
	}

}
