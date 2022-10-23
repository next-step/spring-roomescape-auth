package nextstep.auth;

import nextstep.member.MemberService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class ApiConfig implements WebMvcConfigurer {

    private final AuthorizationExtractor authorizationExtractor;
    private final MemberService memberService;

    public ApiConfig(AuthorizationExtractor authorizationExtractor, MemberService memberService) {
        this.authorizationExtractor = authorizationExtractor;
        this.memberService = memberService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
        resolvers.add(new AuthenticationPrincipalArgumentResolver(authorizationExtractor, memberService));
    }
}
