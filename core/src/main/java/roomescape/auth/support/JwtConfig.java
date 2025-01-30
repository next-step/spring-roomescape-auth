package roomescape.auth.support;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JWT 설정과 관련한 환경변수를 관리하는 컴포넌트
 */
@Component
public class JwtConfig {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expiration-time}")
    private long validityInMilliseconds;

    public String getSecretKey() {
        return this.secretKey;
    }

    public long getValidityInMilliseconds() {
        return this.validityInMilliseconds;
    }
}
