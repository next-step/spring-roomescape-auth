package com.nextstep.web.common;

import nextstep.common.BusinessException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthorizationExtractor {

    public String extractToken(HttpServletRequest request) {
        try {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            return header.split(" ")[1];
        } catch (Exception e) {
            throw new BusinessException("인증 토큰이 올바르지 않습니다.");
        }
    }
}
