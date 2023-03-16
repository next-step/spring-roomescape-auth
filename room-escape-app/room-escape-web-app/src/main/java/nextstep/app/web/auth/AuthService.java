package nextstep.app.web.auth;

import nextstep.app.web.member.adapter.in.MemberService;
import nextstep.core.member.in.MemberResponse;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService {
    private final JwtTokenProvider tokenProvider;
    private final MemberService memberService;

    public AuthService(JwtTokenProvider tokenProvider, MemberService memberService) {
        this.tokenProvider = tokenProvider;
        this.memberService = memberService;
    }

    public TokenResponse createToken(TokenRequest request) {
        MemberResponse member = memberService.login(request.to());
        var token = tokenProvider.createToken(member.getId().toString(), Collections.emptyList());
        return TokenResponse.from(token);
    }
}
