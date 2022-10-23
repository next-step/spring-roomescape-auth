package nextstep.config;

import java.util.List;
import nextstep.auth.AdminValidationInterceptor;
import nextstep.auth.AuthMemberArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

    private AuthMemberArgumentResolver authMemberArgumentResolver;
    private AdminValidationInterceptor adminValidationInterceptor;

    public AuthConfig(AuthMemberArgumentResolver authMemberArgumentResolver,
        AdminValidationInterceptor adminValidationInterceptor) {
        this.authMemberArgumentResolver = authMemberArgumentResolver;
        this.adminValidationInterceptor = adminValidationInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminValidationInterceptor)
            .addPathPatterns("/admin/**");
    }
}
