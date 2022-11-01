package nextstep.auth;

import nextstep.member.Member;
import nextstep.member.MemberDao;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private static final String LOGIN_INFO_CLAIM_NAME = "member";

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse authorize(TokenRequest request) {
        final Member member = memberDao.findByUsername(request.getUsername());
        if (!member.checkPassword(request.getPassword())) {
            throw new AuthenticationException("Login failed");
        }
        final String accessToken = jwtTokenProvider.createTokenWithClaim(LOGIN_INFO_CLAIM_NAME, LoginInfo.of(member));
        return TokenResponse.of(accessToken);
    }

    public LoginInfo loginInfoFromToken(String token) {
        if (jwtTokenProvider.isTokenAlive(token)) {
            return jwtTokenProvider.getClaim(token, LOGIN_INFO_CLAIM_NAME, LoginInfo.class);
        }
        throw new AuthenticationException("Invalid token");
    }
}
