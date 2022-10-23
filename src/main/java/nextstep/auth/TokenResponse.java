package nextstep.auth;

public class TokenResponse {
    public String accessToken;

    public TokenResponse() {
    }

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public static TokenResponse of(String token) {
        return new TokenResponse(token);
    }

    public String getAccessToken() {
        return accessToken;
    }
}
