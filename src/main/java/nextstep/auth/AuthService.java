package nextstep.auth;

import nextstep.member.Member;
import nextstep.member.MemberService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {
    private static final String INVALID_LOGIN_REQUEST = "요청과 일치하는 회원이 존재하지 않습니다.";
    private static final String ROLE_USER = "USER";

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    public String createToken(String username, String password) {
        Member member = Optional.ofNullable(memberService.findByUsername(username))
                .filter(it -> it.checkRightPassword(password))
                .orElseThrow(this::invalidLoginException);

        return jwtTokenProvider.createToken(member.getId().toString(), List.of(ROLE_USER));
    }

    private IllegalArgumentException invalidLoginException() {
        return new IllegalArgumentException(INVALID_LOGIN_REQUEST);
    }
}
