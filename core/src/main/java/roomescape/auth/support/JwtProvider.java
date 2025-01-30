package roomescape.auth.support;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import roomescape.auth.application.dto.JwtPayload;
import roomescape.domain.common.ClockHolder;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final ClockHolder clockHolder;
    private final JwtConfig jwtConfig;

    public String createToken(final JwtPayload jwtPayload) {
        final Date issuedAt = Date.from(clockHolder.getCurrentSeoulZonedDateTime().toInstant());
        final Date expirationDate = new Date(issuedAt.getTime() + jwtConfig.getValidityInMilliseconds());

        return Jwts.builder()
                .claim("role", jwtPayload.role())
                .claim("email", jwtPayload.email())
                .issuedAt(issuedAt)
                .expiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes())) // todo: Secret Key 객체를 jwtConfig에서 가져오도록 수정
                .compact();
    }
}
