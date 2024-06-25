package roomescape.auth.application;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.auth.ui.dto.LoginCheckResponse;
import roomescape.auth.ui.dto.LoginRequest;
import roomescape.exception.BadRequestException;
import roomescape.exception.NotFoundException;
import roomescape.exception.UnauthorizedException;
import roomescape.member.application.PasswordEncoder;
import roomescape.member.domain.MemberRepository;
import roomescape.member.domain.entity.Member;

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
        String encodedPassword = passwordEncoder.encode(loginRequest.password());
        Member member = memberRepository.findByEmailAndPassword(loginRequest.email(), encodedPassword)
                .orElseThrow(() -> BadRequestException.of("이메일 또는 비밀번호를 확인해주세요."));

        String token = jwtTokenProvider.createToken(member);
        return cookieUtils.createCookie("token", token);
    }

    public Long getMemberIdFromCookies(Cookie[] cookies) {
        String token = cookieUtils
                .getCookieByName(cookies, "token")
                .orElseThrow(() -> UnauthorizedException.of("토큰이 없습니다."))
                .getValue();
        boolean isInvalidToken = !jwtTokenProvider.validateToken(token);

        if (isInvalidToken) {
            throw UnauthorizedException.of("토큰이 만료되었습니다.");
        }
        return jwtTokenProvider.extractMemberId(token);
    }

    public LoginCheckResponse findMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("id에 일치하는 사용자를 찾을 수 없습니다."));
        return new LoginCheckResponse(member.getName());
    }
}
