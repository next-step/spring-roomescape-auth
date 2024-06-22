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
    private final CookieUtils cookieUtils;

    public LoginService(
            MemberRepository memberRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider,
            CookieUtils cookieUtils) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.cookieUtils = cookieUtils;
    }

    public Cookie login(LoginRequest loginRequest) {
        Long memberId = memberRepository.findByEmailAndPassword(
                        loginRequest.email(),
                        passwordEncoder.encode(loginRequest.password()))
                .orElseThrow(() -> BadRequestException.of("이메일 또는 비밀번호를 확인해주세요."))
                .getId();

        String token = jwtTokenProvider.createToken(memberId);
        return cookieUtils.createCookie("token", token);
    }
}
