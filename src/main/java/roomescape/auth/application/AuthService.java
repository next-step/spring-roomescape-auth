package roomescape.auth.application;

import org.springframework.stereotype.Service;

import roomescape.auth.dto.CheckUserInfoResponse;
import roomescape.jwt.JwtTokenProvider;
import roomescape.user.domain.User;
import roomescape.user.domain.repository.UserRepository;
import roomescape.auth.dto.LoginRequest;
import roomescape.user.exception.PasswordNotMatchException;
import roomescape.user.exception.UserNotFoundException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다"));
        if (!user.matchPassword(loginRequest.password())) {
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다");
        }

        return jwtTokenProvider.createJwt(user.getEmail());
    }

    public CheckUserInfoResponse checkUserInfo(String accessToken) {
        String email = jwtTokenProvider.getEmail(accessToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다"));
        return new CheckUserInfoResponse(user.getName());
    }

    public void logout(String accessToken) {
        String email = jwtTokenProvider.getEmail(accessToken);
        userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다"));
    }
}
