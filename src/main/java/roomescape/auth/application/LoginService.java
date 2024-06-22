package roomescape.auth.application;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.auth.ui.dto.LoginRequest;
import roomescape.exception.BadRequestException;
import roomescape.member.application.PasswordEncoder;
import roomescape.member.domain.MemberRepository;

@Service
public class LoginService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(
            MemberRepository memberRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Cookie login(LoginRequest loginRequest) {
        Long memberId = memberRepository.findByEmailAndPassword(
                        loginRequest.email(),
                        passwordEncoder.encode(loginRequest.password()))
                .orElseThrow(() -> BadRequestException.of("이메일 또는 비밀번호를 잘못 입력했습니다."))
                .getId();

        String token = jwtTokenProvider.createToken(memberId);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
