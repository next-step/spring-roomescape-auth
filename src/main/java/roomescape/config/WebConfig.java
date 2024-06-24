package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.argumentResolver.LoginArgumentResolver;
import roomescape.domain.member.service.MemberService;
import roomescape.interceptor.RoleCheckInterceptor;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MemberService memberService;

    public WebConfig(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginArgumentResolver(memberService));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RoleCheckInterceptor(memberService)).addPathPatterns("/admin/**");
    }
}
