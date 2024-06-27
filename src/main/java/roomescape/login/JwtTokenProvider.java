package roomescape.login;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @Value("${security.token.secret-key}")
    private String secretKey;

    @Value("${security.token.valid-time}")
    private long validTime;

    public String createToken(Long id, String email, String name) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + validTime);

        return Jwts.builder()
            .claim("id", id)
            .claim("email", email)
            .claim("name", name)
            .setIssuedAt(now)
            .setExpiration(exp)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    public Long getId(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("id", Long.class);
    }

    public String getEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("email", String.class);
    }

    public String getName(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("name", String.class);
    }

    public LoginMember getLoginMember(String token) {
        return new LoginMember(getId(token), getEmail(token), getName(token));
    }
}
