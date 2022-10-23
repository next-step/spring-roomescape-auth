package nextstep.auth;

import nextstep.member.Member;
import nextstep.member.MemberService;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthorizationExtractor authorizationExtractor;
    private final MemberService memberService;

    public AuthenticationPrincipalArgumentResolver(AuthorizationExtractor authorizationExtractor, MemberService memberService) {
        this.authorizationExtractor = authorizationExtractor;
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
       return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Member resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Long userId = authorizationExtractor.extractClaims(
                webRequest.getNativeRequest(HttpServletRequest.class)
        );
        return memberService.findById(userId);
    }
}
