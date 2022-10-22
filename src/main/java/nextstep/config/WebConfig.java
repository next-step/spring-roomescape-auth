package nextstep.config;

import nextstep.auth.LoginInfoArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginInfoArgumentResolver loginInfoArgumentResolver;

    public WebConfig(LoginInfoArgumentResolver loginInfoArgumentResolver) {
        this.loginInfoArgumentResolver = loginInfoArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginInfoArgumentResolver);
    }
}
