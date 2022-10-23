package nextstep.application.dto.auth;

public class AuthResponse {

    private String accessToken;

    private AuthResponse() {
    }

    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
