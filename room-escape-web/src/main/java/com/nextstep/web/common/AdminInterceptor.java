package com.nextstep.web.common;

import com.nextstep.web.auth.JwtTokenProvider;
import com.nextstep.web.member.service.MemberService;
import nextstep.common.BusinessException;
import nextstep.domain.member.Role;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    private final AuthorizationExtractor authorizationExtractor;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminInterceptor(AuthorizationExtractor authorizationExtractor, JwtTokenProvider jwtTokenProvider) {
        this.authorizationExtractor = authorizationExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = authorizationExtractor.extractToken(request); // 아니라면 Header로 넘어온 토큰을 해체합니다.
        List<String> roles = jwtTokenProvider.getRoles(token);
        roles.stream()
                .filter(Role.ADMIN.name()::equals)
                .findAny()
                .orElseThrow(() ->  new BusinessException("어드민 계정이 아닙니다."));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

}
