package roomescape.member.infra;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class MemberConfiguration implements WebMvcConfigurer {

    private final MemberArgumentResolver memberArgumentResolver;
    private final MemberHandlerInterceptor memberHandlerInterceptor;

    public MemberConfiguration(MemberArgumentResolver memberArgumentResolver, MemberHandlerInterceptor memberHandlerInterceptor) {
        this.memberArgumentResolver = memberArgumentResolver;
        this.memberHandlerInterceptor = memberHandlerInterceptor;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        System.out.println("addInterceptors");
        registry.addInterceptor(memberHandlerInterceptor)
                .addPathPatterns("/admin/**");
    }

}
