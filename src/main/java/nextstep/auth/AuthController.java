package nextstep.auth;

import java.util.Collections;
import nextstep.member.Member;
import nextstep.member.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login/token")
    TokenResponse createToken(@RequestBody TokenRequest tokenRequest) {
        String username = tokenRequest.getUsername();
        String password = tokenRequest.getPassword();

        Member member = memberService.findByUsername(username);
        if (member == null || member.checkWrongPassword(password)) {
            throw new AuthenticationException();
        }
        return new TokenResponse(
            jwtTokenProvider.createToken(username, Collections.singletonList("USER"))
        );
    }
}
