package nextstep.auth;

import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.member.Role;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class AuthService {
    private static final String BEARER = "Bearer ";
    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;


    public AuthService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest request) {
        Member member = memberDao.findByUsername(request.getUsername());
        if (member == null || member.checkWrongPassword(request.getPassword())) {
            throw new AuthenticationException("인증에 실패하였습니다.");
        }
        String token = jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getRole());

        return new TokenResponse(token);
    }

    public String extractToken(HttpServletRequest request) {
        try {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            String token = header.replace(BEARER, "");
            if (!validateToken(token)) {
                throw new AuthenticationException("인증에 실패하였습니다.");
            }

            return token;
        } catch (Exception e) {
            throw new AuthenticationException("인증에 실패하였습니다.");
        }
    }

    private boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    public String getPrincipal(String token) {
        return jwtTokenProvider.getPrincipal(token);
    }

    public Role getRole(String token) {
        return jwtTokenProvider.getRole(token);
    }
}
