package roomescape.member.infra;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class MemberConfiguration implements WebMvcConfigurer {

    private final MemberArgumentResolver memberArgumentResolver;

    public MemberConfiguration(final MemberArgumentResolver memberArgumentResolver) {
        this.memberArgumentResolver = memberArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
    }
}
