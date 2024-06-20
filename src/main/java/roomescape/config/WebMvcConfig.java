package roomescape.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import roomescape.auth.LoginUserArgumentResolver;
import roomescape.jwt.JwtTokenProvider;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

    public WebMvcConfig(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserArgumentResolver(jwtTokenProvider));
    }
}
