package roomescape.auth.support;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import roomescape.auth.application.dto.LoginToken;

import java.util.Arrays;
import java.util.Optional;

/**
 * JwtProvider 컴포넌트가 수행하는 JWT 생성외의 작업(parsing, validate)을 수행하는 컴포넌트
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtSupporter {

    private final JwtConfig jwtConfig;

    public boolean isValidToken(final LoginToken loginToken) {
        if (!StringUtils.hasText(loginToken.accessToken())) {
            return false;
        }

        final String accessToken = loginToken.accessToken();

        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes()))
                    .build()
                    .parseSignedClaims(accessToken);

            return true;
        } catch (JwtException e) {
            log.warn("JWT parsing failed: %s".formatted(e.getMessage()), e);
            return false;
        }
    }

    public Claims extractClaims(LoginToken loginToken) {
        Jws<Claims> parsedJws = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes()))
                .build()
                .parseSignedClaims(loginToken.accessToken());

        return parsedJws.getPayload();
    }

    public LoginToken extractLoginToken(final HttpServletRequest servletRequest) {
        final Cookie[] cookies = servletRequest.getCookies();

        if (cookies == null) {
            throw new IllegalArgumentException("Empty cookies in servletRequest");
        }

        Optional<LoginToken> loginToken = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("accessToken"))
                .findFirst()
                .map(cookie -> new LoginToken(cookie.getValue()));

        return loginToken.orElseThrow(() -> new IllegalArgumentException("Empty accessToken in cookies"));
    }
}
