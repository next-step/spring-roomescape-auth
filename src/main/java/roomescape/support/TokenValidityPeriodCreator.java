package roomescape.support;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class TokenValidityPeriodCreator {

    private static final long TOKEN_EXPIRATION_MINUTES = 60;

    private TokenValidityPeriodCreator() {
        throw new UnsupportedOperationException("인스턴스화 할 수 없는 클래스입니다.");
    }


    public static TokenValidityPeriod createExpirationDate() {
        Instant now = Instant.now();
        Instant expirationDateTime = now.plus(Duration.ofMinutes(TOKEN_EXPIRATION_MINUTES));

        Date createdAt = Date.from(now);
        Date expirationAt = Date.from(expirationDateTime);

        return new TokenValidityPeriod(createdAt, expirationAt);
    }


    public record TokenValidityPeriod(
            Date createdAt,
            Date expirationAt
    ) {
    }
}
