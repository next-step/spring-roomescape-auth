package com.nextstep.web.common;

import com.nextstep.web.auth.AuthenticationException;
import com.nextstep.web.auth.JwtTokenProvider;
import nextstep.common.BusinessException;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationPrincipalArgumentResolver(JwtTokenProvider provider) {
        this.jwtTokenProvider = provider;
    }

    // resolveArgument 메서드가 동작하는 조건을 정의하는 메서드
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 파라미터 중 @AuthenticationPrincipal이 붙은 경우 동작하게 설정
        return parameter.hasParameterAnnotation(LoginMemberPrincipal.class);
    }

    // supportsParameter가 true인 경우 동작하는 메서드
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String token = extractToken((HttpServletRequest) webRequest.getNativeRequest());
        jwtTokenProvider.validateToken(token);
        String principal = jwtTokenProvider.getPrincipal(token);
        return LoginMember.from(principal);
    }

    private String extractToken(HttpServletRequest request) {
        try {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            return header.split(" ")[1];
        } catch (Exception e) {
            throw new BusinessException("");
        }
    }
}