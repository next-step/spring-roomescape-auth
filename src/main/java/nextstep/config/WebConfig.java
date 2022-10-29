package nextstep.config;

import nextstep.auth.AdminInterceptor;
import nextstep.auth.LoginInfoArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginInfoArgumentResolver loginInfoArgumentResolver;
    private final AdminInterceptor adminInterceptor;

    public WebConfig(LoginInfoArgumentResolver loginInfoArgumentResolver, AdminInterceptor adminInterceptor) {
        this.loginInfoArgumentResolver = loginInfoArgumentResolver;
        this.adminInterceptor = adminInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginInfoArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
            .addPathPatterns("/admin/**");
    }
}
