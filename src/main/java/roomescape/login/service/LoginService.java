package roomescape.login.service;

import org.springframework.stereotype.Service;
import roomescape.login.JwtTokenProvider;
import roomescape.login.LoginMember;
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
        LoginMember member = memberService.getLoginMemberByEmailAndPassword(email, password);

        return jwtTokenProvider.createToken(member.getId(), member.getEmail(), member.getName());
    }
}
