package nextstep.auth;

import nextstep.member.Member;
import nextstep.member.MemberService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;
import java.util.Optional;

@Component
public class LoginMemberIdArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String AUTHORIZATION = "authorization";
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public LoginMemberIdArgumentResolver(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMemberId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String principal = jwtTokenProvider.getPrincipalBy(Objects.requireNonNull(webRequest.getHeader(AUTHORIZATION)));

        Member member = Optional.ofNullable(memberService.findById(Long.valueOf(principal)))
                .orElseThrow(() -> new IllegalArgumentException("인증 정보가 유효하지 않습니다."));

        return member.getId();
    }
}
