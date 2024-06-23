package roomescape.jwt;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Arrays;
import java.util.Date;

import jakarta.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import roomescape.user.domain.Role;
import roomescape.user.domain.User;

@Component
public class JwtTokenProvider {

    private static final String EMAIL_CLAIM = "email";
    private static final String ROLE_CLAIM = "role";
    private static final String TOKEN_COOKIE_NAME = "token";
    private static final String EMPTY_TOKEN = "";

    private final SecretKey secretKey;
    private final long expiredMilliseconds;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret, @Value("${jwt.expire}") long validityInMilliseconds) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(UTF_8));
        this.expiredMilliseconds = validityInMilliseconds;
    }

    public String createJwt(User user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim(EMAIL_CLAIM, user.getEmail())
                .claim(ROLE_CLAIM, user.getRole())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMilliseconds))
                .signWith(secretKey)
                .compact();
    }

    public Role getRoleFromCookies(Cookie[] cookies) {
        String token = extractTokenFromCookie(cookies);
        String role = extractPayload(token).get(ROLE_CLAIM, String.class);
        return Role.valueOf(role);
    }

    public Long getUserIdFromCookies(Cookie[] cookies) {
        String token = extractTokenFromCookie(cookies);
        return Long.parseLong(extractPayload(token).getSubject());
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> TOKEN_COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(EMPTY_TOKEN);
    }

    private Claims extractPayload(final String accessToken) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();
    }
}
