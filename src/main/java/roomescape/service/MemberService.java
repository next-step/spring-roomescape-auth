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

        Member member = memberDao.findByEmail(email)
                .orElseThrow(() -> new BusinessException("존재하지 않는 사용자입니다.", HttpStatus.BAD_REQUEST));
        if (!member.getPassword().equals(password)) {
            throw new BusinessException("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        return jwtTokenProvider.createToken(email);
    }

    public LoginResponse loginCheck(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException("토큰이 존재하지 않습니다", HttpStatus.BAD_REQUEST);
        }

        String email = jwtTokenProvider.getPayload(token);
        Member member = memberDao.findByEmail(email)
                .orElseThrow(() -> new BusinessException("존재하지 않는 사용자입니다.", HttpStatus.BAD_REQUEST));

        return new LoginResponse(member.getName());
    }
}
