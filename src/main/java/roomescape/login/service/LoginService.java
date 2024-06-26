package roomescape.login.service;

import org.springframework.stereotype.Service;
import roomescape.login.JwtTokenProvider;
import roomescape.login.LoginMember;

@Service
public class LoginService {

    private final LoginMemberService loginMemberService;

    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(LoginMemberService loginMemberService, JwtTokenProvider jwtTokenProvider) {
        this.loginMemberService = loginMemberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createToken(String email, String password) {
        LoginMember member = loginMemberService.getLoginMember(email, password);

        return jwtTokenProvider.createToken(member.getId(), member.getEmail(), member.getName());
    }
}
