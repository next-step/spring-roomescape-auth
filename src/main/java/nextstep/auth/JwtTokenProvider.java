package nextstep.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    private final ObjectMapper mapper;

    public JwtTokenProvider(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public String createTokenWithClaim(String claimName, Object claimValue) {
        final Date now = new Date();
        final Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
            .claim(claimName, claimValue)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    public <T> T getClaim(String token, String claimName, Class<T> claimType) {
        try {
            final Object claim = getClaims(token).get(claimName);
            return mapper.convertValue(claim, claimType);
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthenticationException("Invalid token");
        }
    }

    public boolean isTokenAlive(String token) {
        try {
            final Date now = new Date();
            final Date expiration = getClaims(token).getExpiration();
            return expiration.after(now);
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthenticationException("Invalid token");
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();
    }
}
