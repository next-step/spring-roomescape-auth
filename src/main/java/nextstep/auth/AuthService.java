package nextstep.auth;

import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.jwt.JwtTokenProvider;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import org.springframework.stereotype.Service;

import java.util.List;

import static nextstep.auth.exception.AuthenticationException.Status;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(String username, String password) throws AuthenticationException {
        Member member = memberDao.findByUsername(username).orElseThrow(() -> new AuthenticationException(Status.WRONG_USERNAME));
        if (member.checkWrongPassword(password)) {
            throw new AuthenticationException(Status.WRONG_PASSWORD);
        }
        return jwtTokenProvider.createToken(member.getUuid(), List.of("USER"));
    }
}
