package roomescape.member.dto;

public class TokenResponseDto {
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public TokenResponseDto() {
    }

    public TokenResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "{" +
                "accessToken='" + accessToken + '\'' +
                '}';
    }
}
