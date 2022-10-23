package nextstep.config;

import java.util.List;
import nextstep.application.controller.auth.AuthArgumentResolver;
import nextstep.application.controller.auth.AuthExtractor;
import nextstep.application.service.auth.JwtTokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthExtractor authExtractor;
    private final JwtTokenProvider jwtTokenProvider;

    public WebConfig(
        AuthExtractor authExtractor,
        JwtTokenProvider jwtTokenProvider
    ) {
        this.authExtractor = authExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
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
