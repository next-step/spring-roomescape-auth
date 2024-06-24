package roomescape.auth.application;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.exception.UnauthorizedException;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length.hour}")
    private long validTimeInHour;

    public String createToken(Long memberId) {
        Date now = new Date();
        Date validityTime = new Date(now.getTime() + validTimeInHour * 60 * 60 * 1000);

        return Jwts.builder()
                .setSubject(memberId.toString())
                .setIssuedAt(now)
                .setExpiration(validityTime)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Long extractMemberId(String token) {
        String memberId = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return Long.valueOf(memberId);
    }

    public boolean validateToken(String token) {
        if (token.isEmpty()) {
            throw UnauthorizedException.of("토큰이 비어있습니다.");
        }
        try {
            Jws<Claims> claim = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            return !claim.getBody().getExpiration().before(new Date());
        }
        catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }
}
