package roomescape.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.LoginResponse;
import roomescape.exception.BusinessException;
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

        if (!member.getPassword().equals(password)) {
            throw new BusinessException("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    private Member findByEmail(String email) {
        Member member = memberDao.findByEmail(email)
                .orElseThrow(() -> new BusinessException("존재하지 않는 사용자입니다.", HttpStatus.BAD_REQUEST));
        return member;
    }

    public LoginResponse loginCheck(String token) {
        validateToken(token);

        String email = jwtTokenProvider.getPayload(token);
        Member member = findByEmail(email);

        return new LoginResponse(member.getName());
    }

    private void validateToken(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException("토큰이 존재하지 않습니다", HttpStatus.BAD_REQUEST);
        }

        if (!jwtTokenProvider.validateToken(token)) {
            throw new BusinessException("유효하지 않은 토큰입니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
