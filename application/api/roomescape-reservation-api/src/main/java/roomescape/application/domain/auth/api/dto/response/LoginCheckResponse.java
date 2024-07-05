package roomescape.application.domain.auth.api.dto.response;


import roomescape.domain.user.User;

public class LoginCheckResponse {

    private final String name;

    public LoginCheckResponse(String name) {
        this.name = name;
    }

    public static LoginCheckResponse from(User user) {
        return new LoginCheckResponse(user.getName().name());
    }

    public String getName() {
        return name;
    }
}
