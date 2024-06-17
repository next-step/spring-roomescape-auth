package roomescape.service;

import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.LoginResponse;
import roomescape.exception.custom.PasswordMismatchException;
import roomescape.exception.custom.TokenNotFoundException;
import roomescape.exception.custom.UserNotFoundException;
import roomescape.repository.JdbcMemberDao;
import roomescape.util.JwtTokenProvider;

@Service
public class MemberService {
    private final JwtTokenProvider jwtTokenProvider;
    private final JdbcMemberDao memberDao;

    public MemberService(JwtTokenProvider jwtTokenProvider, JdbcMemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public String tokenLogin(LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        validateMemberCredentials(email, password);

        return jwtTokenProvider.createToken(email);
    }

    private void validateMemberCredentials(String email, String password) {
        Member member = findByEmail(email);

        if (!member.checkPassword(password)) {
            throw new PasswordMismatchException();
        }
    }

    private Member findByEmail(String email) {
        return memberDao.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

    public LoginResponse loginCheck(String token) {
        validateToken(token);

        String email = jwtTokenProvider.getPayload(token);
        Member member = findByEmail(email);

        return new LoginResponse(member.getName());
    }

    private void validateToken(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new TokenNotFoundException();
        }
        jwtTokenProvider.validateToken(token);
    }
}
