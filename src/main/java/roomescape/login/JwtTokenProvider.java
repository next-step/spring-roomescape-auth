package roomescape.login;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.member.MemberRole;

@Component
public class JwtTokenProvider {

    @Value("${security.token.secret-key}")
    private String secretKey;

    @Value("${security.token.valid-time}")
    private long validTime;

    public String createToken(Long id, String name, MemberRole memberRole) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + validTime);

        return Jwts.builder()
            .claim("id", id)
            .claim("name", name)
            .claim("role", memberRole.name())
            .setIssuedAt(now)
            .setExpiration(exp)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    public LoginMember getLoginMember(String token) {
        Long id = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody()
            .get("id", Long.class);

        String name = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody()
            .get("name", String.class);

        return new LoginMember(id, name, getMemberRole(token));
    }

    public MemberRole getMemberRole(String token) {
        String role = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody()
            .get("role", String.class);

        return MemberRole.of(role);
    }
}
