package nextstep.auth;

import nextstep.member.Member;
import nextstep.member.MemberDao;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private MemberDao memberDao;
    private JwtTokenProvider jwtTokenProvider;

    public LoginService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(TokenRequest request) {
        Member member = findMemberByUsername(request.getUsername());
        member.checkWrongPassword(request.getPassword());
        String token = jwtTokenProvider.createToken(member.getId().toString(), member.getRole());
        return new TokenResponse(token);
    }

    private Member findMemberByUsername(String username) {
        Member member = memberDao.findByUsername(username);
        if (member == null) {
            throw new IllegalArgumentException("Username 에 해당하는 사용자를 찾을 수 없습니다.");
        }
        return member;
    }
}
