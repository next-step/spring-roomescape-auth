package nextstep.app.web.auth;

import nextstep.app.web.member.adapter.in.MemberService;
import nextstep.core.member.in.MemberResponse;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String TOKEN_FORMAT = "Bearer ";

    private final JwtTokenProvider tokenProvider;
    private final AuthorizationResolver authorizationResolver;
    private final MemberService memberService;

    public LoginMemberArgumentResolver(JwtTokenProvider tokenProvider, AuthorizationResolver authorizationResolver, MemberService memberService) {
        this.tokenProvider = tokenProvider;
        this.authorizationResolver = authorizationResolver;
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String token = authorizationResolver.resolve(webRequest.getHeader("authorization"));
        String principal = tokenProvider.getPrincipal(token);

        MemberResponse member = memberService.findMember(Long.parseLong(principal));
        return member.getId();
    }
}
