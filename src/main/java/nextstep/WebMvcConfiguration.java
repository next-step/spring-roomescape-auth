package nextstep;

import nextstep.auth.LoginMemberArgumentResolver;
import nextstep.auth.LoginService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private LoginService loginService;

    public WebMvcConfiguration(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new LoginMemberArgumentResolver(loginService));
    }
}
