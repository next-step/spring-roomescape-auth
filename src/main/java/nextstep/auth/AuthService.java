package nextstep.auth;

import nextstep.member.Member;
import nextstep.member.MemberDao;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        List<String> roles = Arrays.stream(member.getRole().split(",")).collect(Collectors.toList());
        return jwtTokenProvider.createToken(String.valueOf(member.getId()), roles);
    }

    public Long findPrincipal(String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new AuthenticationException();
        }
        return Long.valueOf(jwtTokenProvider.getPrincipal(accessToken));
    }
}
