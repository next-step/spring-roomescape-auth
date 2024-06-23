package roomescape.web.config;

import java.util.List;

import roomescape.auth.JwtTokenProvider;
import roomescape.service.AuthService;
import roomescape.web.handler.LoginCheckFilter;
import roomescape.web.handler.LoginMemberArgumentResolver;
import roomescape.web.handler.RoleInterceptor;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final LoginMemberArgumentResolver loginMemberArgumentResolver;

	private final JwtTokenProvider jwtTokenProvider;

	private final AuthService authService;

	WebConfig(LoginMemberArgumentResolver loginMemberArgumentResolver, JwtTokenProvider jwtTokenProvider,
			AuthService authService) {
		this.loginMemberArgumentResolver = loginMemberArgumentResolver;
		this.jwtTokenProvider = jwtTokenProvider;
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

	@Bean
	public FilterRegistrationBean<LoginCheckFilter> loginCheckFilter() {
		FilterRegistrationBean<LoginCheckFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new LoginCheckFilter(this.jwtTokenProvider));
		registrationBean.addUrlPatterns("/");
		registrationBean.addUrlPatterns("/admin/*");
		registrationBean.addUrlPatterns("/reservation/*");
		return registrationBean;
	}

}
