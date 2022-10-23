package nextstep.auth.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    public String createToken(String principal, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(principal);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .claim("roles", roles)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public TokenBody getTokenBody(String token) {
        Claims body = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return new TokenBody(body.getSubject(), body.get("roles", List.class));
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public static class TokenBody {
        private final String body;
        private final List<String> roles;

        public TokenBody(String body, List<String> roles) {
            this.body = body;
            this.roles = roles;
        }

        public String getBody() {
            return body;
        }

        public List<String> getRoles() {
            return roles;
        }
    }
}
