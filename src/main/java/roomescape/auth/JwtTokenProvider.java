package roomescape.auth;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import roomescape.config.JwtProperties;
import roomescape.web.controller.dto.MemberResponse;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;

import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

	private static final String NAME = "name";

	private static final String ROLE = "role";

	private static final String EMAIL = "email";

	private final JwtProperties jwtProperties;

	JwtTokenProvider(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
	}

	public String createToken(MemberResponse memberResponse) {
		return Jwts.builder()
			.subject(memberResponse.email())
			.claim(NAME, memberResponse.name())
			.claim(ROLE, memberResponse.role())
			.claim(EMAIL, memberResponse.email())
			.issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis() + this.jwtProperties.getTokenExpireLength()))
			.signWith(Keys.hmacShaKeyFor(this.jwtProperties.getTokenSecretKey().getBytes()))
			.compact();
	}

	public Claims validateToken(String token) {
		try {
			return Jwts.parser()
				.verifyWith(Keys.hmacShaKeyFor(this.jwtProperties.getTokenSecretKey().getBytes()))
				.clockSkewSeconds(this.jwtProperties.getClockSkewSeconds())
				.build()
				.parseSignedClaims(token)
				.getPayload();
		}
		catch (IllegalArgumentException | ExpiredJwtException ex) {
			throw new RoomEscapeException(ErrorCode.EXPIRED_LOGIN_TOKEN);
		}
	}

}
