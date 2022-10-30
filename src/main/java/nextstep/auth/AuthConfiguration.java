package nextstep.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfiguration implements WebMvcConfigurer {
    private final LoginMemberIdArgumentResolver loginMemberIdArgumentResolver;

    public AuthConfiguration(LoginMemberIdArgumentResolver loginMemberIdArgumentResolver) {
        this.loginMemberIdArgumentResolver = loginMemberIdArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberIdArgumentResolver);
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
}
