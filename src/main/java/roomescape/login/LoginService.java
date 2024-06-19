package roomescape.login;

import org.springframework.stereotype.Service;
import roomescape.error.exception.PasswordNotMatchedException;
import roomescape.member.service.MemberService;

@Service
public class LoginService {

    private final MemberService memberService;

    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createToken(String email, String password) {
        if (!memberService.isMember(email, password)) {
            throw new PasswordNotMatchedException();
        }

        return jwtTokenProvider.createToken(email);
    }

    public LoginResponse checkToken(String token) {
        String email = jwtTokenProvider.getPayload(token);

        return new LoginResponse(memberService.findNameByEmail(email));
    }
}
