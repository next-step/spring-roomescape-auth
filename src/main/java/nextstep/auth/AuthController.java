package nextstep.auth;

import nextstep.member.Member;
import nextstep.member.MemberDao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthController(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest request) {
        Member member = memberDao.findByUsername(request.getUsername());

        if (member == null || member.checkWrongPassword(request.getPassword())) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getRoles());
        TokenResponse response = new TokenResponse(token);
        return ResponseEntity.ok(response);
    }
}
