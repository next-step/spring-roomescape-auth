package roomescape.apply.auth.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.apply.auth.application.exception.IllegalTokenException;
import roomescape.apply.auth.ui.dto.LoginResponse;
import roomescape.support.TokenValidityPeriodCreator;

import javax.crypto.SecretKey;
import java.util.LinkedHashMap;

@Service
public class JwtTokenManager {
    public static final String NOT_VALID_TOKEN_ERROR = "잘못된 토큰입니다. 재 로그인 해주세요.";
    public static final String EXPIRED_TOKEN_ERROR = "이미 만료된 토큰입니다. 재 로그인 해주세요.";
    private static final String ROLE_CLAIM = "role";
    private static final String NAME_CLAIM = "name";
    private static final String JOINED_NAMES = "joinedNames";

    private final SecretKey secretKey;

    public JwtTokenManager(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateTokenByLoginResponse(LoginResponse loginResponse) {
        var tokenValidityPeriod = TokenValidityPeriodCreator.createExpirationDate();

        return Jwts.builder()
                .subject(loginResponse.email())
                .claim(NAME_CLAIM, loginResponse.name())
                .claim(ROLE_CLAIM, loginResponse.memberRoleNames())
                .issuedAt(tokenValidityPeriod.createdAt())
                .expiration(tokenValidityPeriod.expirationAt())
                .signWith(secretKey)
                .compact();
    }

    public void validateToken(String token) {
        try {
            parseJwt(token);
        } catch (ExpiredJwtException e) {
            throw new IllegalTokenException(EXPIRED_TOKEN_ERROR);
        } catch (RuntimeException e) {
            throw new IllegalTokenException(NOT_VALID_TOKEN_ERROR, e);
        }
    }

    public String getRoleNameFromToken(String token) {
        Object role = parseJwt(token).get(ROLE_CLAIM);
        if (role instanceof LinkedHashMap roleHashMap && (roleHashMap.containsKey(JOINED_NAMES))) {
            return roleHashMap.get(JOINED_NAMES).toString();
        }
        throw new IllegalTokenException();
    }

    private Claims parseJwt(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getEmailBy(String token) {
        return parseJwt(token).getSubject();
    }
}
