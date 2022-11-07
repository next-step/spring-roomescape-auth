package nextstep.auth;

import nextstep.member.Member;
import nextstep.member.MemberService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    public String createToken(String username, String password) {
        Member member = memberService.findByUsername(username);
        if (member.checkWrongPassword(password)) {
            throw new AuthenticationException();
        }
        return jwtTokenProvider.createToken(String.valueOf(member.getId()), Collections.singletonList("ADMIN"));
    }
}
