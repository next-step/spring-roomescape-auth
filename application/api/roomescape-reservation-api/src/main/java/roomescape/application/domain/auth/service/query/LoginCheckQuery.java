package roomescape.application.domain.auth.service.query;

import roomescape.jwt.JwtToken;

public class LoginCheckQuery {

    private final JwtToken token;

    public LoginCheckQuery(JwtToken token) {
        this.token = token;
    }

    public JwtToken getToken() {
        return token;
    }
}
