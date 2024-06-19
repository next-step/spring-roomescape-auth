package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.application.port.in.LoginUseCase;
import roomescape.config.auth.resolver.LoginUserArgumentResolver;
import roomescape.config.interceptor.AdminInterceptor;

@Configuration
public class CustomWebMvcConfigurer implements WebMvcConfigurer {

    private final LoginUseCase loginUseCase;
    private final AdminInterceptor adminInterceptor;

    public CustomWebMvcConfigurer(LoginUseCase loginUseCase, AdminInterceptor adminInterceptor) {
        this.loginUseCase = loginUseCase;
        this.adminInterceptor = adminInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserArgumentResolver(loginUseCase));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**");
    }

}
