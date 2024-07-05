package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.application.CookieUtils;
import roomescape.auth.application.JwtTokenProvider;
import roomescape.auth.ui.argumentresolver.AuthenticatedArgumentResolver;
import roomescape.auth.ui.argumentresolver.LoginArgumentResolver;
import roomescape.auth.ui.interceptor.AdminInterceptor;
import roomescape.auth.ui.interceptor.TokenValidateInterceptor;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtils cookieUtils;

    public WebMvcConfiguration(JwtTokenProvider jwtTokenProvider, CookieUtils cookieUtils) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.cookieUtils = cookieUtils;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/admin/reservation").setViewName("admin/reservation");
        registry.addViewController("/admin/time").setViewName("admin/time");
        registry.addViewController("/admin/theme").setViewName("admin/theme");
        registry.addViewController("/reservation").setViewName("reservation");
        registry.addViewController("/signup").setViewName("signup");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginArgumentResolver(jwtTokenProvider, cookieUtils));
        resolvers.add(new AuthenticatedArgumentResolver(jwtTokenProvider, cookieUtils));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenValidateInterceptor(jwtTokenProvider, cookieUtils))
                .addPathPatterns("/admin/**", "/reservation", "/reservations/**", "/times/**", "/themes/**");
        registry.addInterceptor(new AdminInterceptor(jwtTokenProvider, cookieUtils))
                .addPathPatterns("/admin/**");
    }
}
