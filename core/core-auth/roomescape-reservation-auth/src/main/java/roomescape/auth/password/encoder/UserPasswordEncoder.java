package roomescape.auth.password.encoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import roomescape.domain.user.vo.UserPassword;

@Component
public class UserPasswordEncoder {

    private final PasswordEncoder passwordEncoder;

    public UserPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserPassword encode(UserPassword password) {
        return new UserPassword(passwordEncoder.encode(password.password()));
    }

    public boolean matches(UserPassword rawPassword, UserPassword encodedPassword) {
        return passwordEncoder.matches(rawPassword.password(), encodedPassword.password());
    }
}
