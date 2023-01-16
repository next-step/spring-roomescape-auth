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
    private final MemberService memberService;

    public LoginMemberArgumentResolver(JwtTokenProvider tokenProvider, MemberService memberService) {
        this.tokenProvider = tokenProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String authorization = webRequest.getHeader("authorization");
        if (authorization == null) {
            throw new AuthenticationException("토큰 정보가 없습니다.");
        }
        if (!authorization.startsWith(TOKEN_FORMAT)) {
            throw new AuthenticationException("잘못된 토큰 형식입니다.");
        }

        String token = authorization.replace(TOKEN_FORMAT, "");
        if (!tokenProvider.validateToken(token)) {
            throw new AuthenticationException("잘못된 토큰입니다.");
        }

        String principal = tokenProvider.getPrincipal(token);
        MemberResponse member = memberService.findMember(Long.parseLong(principal));
        return member.getId();
    }
}
