package roomescape.support.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.apply.auth.application.JwtTokenManager;
import roomescape.apply.auth.application.MemberRolePathExtractor;
import roomescape.apply.member.application.MemberFinder;
import roomescape.support.handler.LoginMemberArgumentResolver;
import roomescape.support.handler.MemberRoleAccessInterceptor;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberFinder memberFinder;
    private final JwtTokenManager jwtTokenManager;
    private final MemberRolePathExtractor memberRolePathExtractor;
    private final MemberRoleAccessInterceptor memberRoleAccessInterceptor;

    public WebMvcConfig(MemberFinder memberFinder,
                        JwtTokenManager jwtTokenManager,
                        MemberRolePathExtractor memberRolePathExtractor,
                        MemberRoleAccessInterceptor memberRoleAccessInterceptor
    ) {
        this.memberFinder = memberFinder;
        this.jwtTokenManager = jwtTokenManager;
        this.memberRolePathExtractor = memberRolePathExtractor;
        this.memberRoleAccessInterceptor = memberRoleAccessInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(memberRoleAccessInterceptor)
                .addPathPatterns(memberRolePathExtractor.findURIAnnotatedNeedAccountRole());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new LoginMemberArgumentResolver(jwtTokenManager, memberFinder));
    }
}
