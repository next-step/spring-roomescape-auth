package roomescape.jwt;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import roomescape.user.domain.Role;
import roomescape.user.domain.User;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long expiredMilliseconds;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret, @Value("${jwt.expire}") long validityInMilliseconds) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(UTF_8));
        this.expiredMilliseconds = validityInMilliseconds;
    }

    public String createJwt(User user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMilliseconds))
                .signWith(secretKey)
                .compact();
    }

    public String getEmail(String accessToken) {
        if (accessToken.isEmpty()) {
            return null;
        }
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload()
                .get("email", String.class);
    }

    public Role getRole(String accessToken) {
        if (accessToken.isEmpty()) {
            return null;
        }
        String role = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload()
                .get("role", String.class);
        return Role.valueOf(role);
    }
}
