package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.LoginResponse;
import roomescape.util.JwtTokenProvider;

@Service
public class UserService {
    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "1234";
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String tokenLogin(LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        if (EMAIL.equals(email) && PASSWORD.equals(password)) {
            throw new RuntimeException();
        }
        return jwtTokenProvider.createToken(email);
    }

    public LoginResponse loginCheck() {
        return null;
    }
}
