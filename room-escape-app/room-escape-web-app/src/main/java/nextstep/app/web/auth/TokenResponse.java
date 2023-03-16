package nextstep.app.web.auth;

public class TokenResponse {
    public String accessToken;

    private TokenResponse() {
    }

    private TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public static TokenResponse from(String token) {
        return new TokenResponse(token);
    }

    public String getAccessToken() {
        return accessToken;
    }
}
