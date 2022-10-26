package nextstep.config;

import java.util.List;
import nextstep.application.controller.auth.AuthArgumentResolver;
import nextstep.application.controller.auth.AuthExtractor;
import nextstep.application.controller.auth.AuthInterceptor;
import nextstep.application.service.auth.JwtTokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final AuthExtractor authExtractor;
    private final JwtTokenProvider jwtTokenProvider;

    public WebConfig(
        AuthInterceptor authInterceptor,
        AuthExtractor authExtractor,
        JwtTokenProvider jwtTokenProvider
    ) {
        this.authInterceptor = authInterceptor;
        this.authExtractor = authExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        interceptorRegistry
            .addInterceptor(authInterceptor)
            .addPathPatterns("/admin/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry
            .addMapping("/**")
            .allowedMethods("*")
            .allowedOriginPatterns("*");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new AuthArgumentResolver(authExtractor, jwtTokenProvider));
    }
}
