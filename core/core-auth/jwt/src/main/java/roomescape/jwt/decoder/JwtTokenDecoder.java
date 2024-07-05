package roomescape.jwt.decoder;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.domain.user.vo.UserId;
import roomescape.jwt.JwtToken;

@Component
public class JwtTokenDecoder {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    public UserId decode(JwtToken token) {
        return new UserId(
                Long.parseLong(
                        Jwts.parser()
                                .setSigningKey(secretKey)
                                .parseClaimsJws(token.token())
                                .getBody()
                                .getSubject()
                )
        );
    }
}
