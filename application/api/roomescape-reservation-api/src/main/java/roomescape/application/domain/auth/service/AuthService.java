package roomescape.application.domain.auth.service;

import org.springframework.stereotype.Service;
import roomescape.application.domain.auth.service.command.LoginCommand;
import roomescape.application.domain.auth.service.query.LoginCheckQuery;
import roomescape.application.domain.user.service.component.reader.UserReader;
import roomescape.auth.error.exception.AuthenticationException;
import roomescape.auth.password.encoder.UserPasswordEncoder;
import roomescape.domain.user.User;
import roomescape.domain.user.vo.UserId;
import roomescape.jwt.JwtToken;
import roomescape.jwt.decoder.JwtTokenDecoder;
import roomescape.jwt.provider.JwtTokenProvider;

@Service
public class AuthService {

    private final JwtTokenProvider tokenProvider;
    private final JwtTokenDecoder decoder;
    private final UserPasswordEncoder passwordEncoder;
    private final UserReader userReader;

    public AuthService(
            JwtTokenProvider tokenProvider,
            JwtTokenDecoder decoder,
            UserPasswordEncoder passwordEncoder,
            UserReader userReader
    ) {
        this.tokenProvider = tokenProvider;
        this.decoder = decoder;
        this.passwordEncoder = passwordEncoder;
        this.userReader = userReader;
    }

    public JwtToken login(LoginCommand command) {
        User user = userReader.readByEmail(command.fetchEmail());

        if (!passwordEncoder.matches(command.fetchPassword(), user.getPassword())) {
            throw AuthenticationException.notMatchPassword(command.fetchPassword());
        }

        return tokenProvider.createToken(user.getId());
    }

    public User loginCheck(LoginCheckQuery query) {
        UserId userId = decoder.decode(query.getToken());

        return userReader.readById(userId);
    }
}
