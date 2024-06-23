package roomescape.auth.application;

import org.springframework.stereotype.Service;

import roomescape.auth.dto.CheckUserInfoResponse;
import roomescape.auth.exception.UnAuthorizedException;
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
        User user = userRepository.findByEmail(loginRequest.email()).orElseThrow(UserNotFoundException::new);

        if (user.isNotMatchPassword(loginRequest.password())) {
            throw new PasswordNotMatchException();
        }

        return jwtTokenProvider.createJwt(user);
    }

    public CheckUserInfoResponse checkUserInfo(Long userId) {
        User user = getUserById(userId);
        return new CheckUserInfoResponse(user.getName());
    }

    public void logout(Long userId) {
        getUserById(userId);
    }

    private User getUserById(final Long userId) {
        return userRepository.findById(userId).orElseThrow(UnAuthorizedException::new);
    }
}
