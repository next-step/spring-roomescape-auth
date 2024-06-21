package roomescape.jwt;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import roomescape.auth.exception.UnAuthorizedException;
import roomescape.user.domain.Role;
import roomescape.user.domain.User;

@Component
public class JwtTokenProvider {

    private static final String EMAIL_CLAIM = "email";
    private static final String ROLE_CLAIM = "role";

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

    public String getEmail(String accessToken) {
        return extractPayload(accessToken).get(EMAIL_CLAIM, String.class);
    }

    public Role getRole(String accessToken) {
        String role = extractPayload(accessToken).get(ROLE_CLAIM, String.class);
        return Role.valueOf(role);
    }

    public Long getUserId(String accessToken) {
        String userId = extractPayload(accessToken).getSubject();
        if (userId == null) {
            throw new UnAuthorizedException();
        }
        return Long.parseLong(userId);
    }

    private Claims extractPayload(final String accessToken) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();
    }
}
