package roomescape.application.domain.user.api.dto.response;

import roomescape.domain.user.User;

public class CreateUserResponse {

    private final Long id;

    private final String name;

    private final String email;

    private final String password;

    public CreateUserResponse(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static CreateUserResponse from(User user) {
        return new CreateUserResponse(user.getId().id(), user.getName().name(), user.getEmail().email(), user.getPassword().password());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
