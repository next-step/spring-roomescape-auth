package nextstep;

import nextstep.auth.jwt.JwtTokenProvider;
import nextstep.auth.resolver.AuthResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthResolver(jwtTokenProvider));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*").allowedOriginPatterns("*");
    }
}
