package nextstep.auth;

import nextstep.member.Member;
import nextstep.member.MemberDao;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public String createToken(String username, String password) {
        Member member = memberDao.findByUsername(username);
        if (member.checkWrongPassword(password)) {
            throw new AuthenticationException();
        }
        return jwtTokenProvider.createToken(String.valueOf(member.getId()), Collections.singletonList("ADMIN"));
    }

    public Long findPrincipal(String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new AuthenticationException();
        }
        return Long.valueOf(jwtTokenProvider.getPrincipal(accessToken));
    }
}
