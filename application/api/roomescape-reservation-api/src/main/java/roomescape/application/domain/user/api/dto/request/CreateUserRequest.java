package roomescape.application.domain.user.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import roomescape.application.domain.user.service.command.CreateUserCommand;

public class CreateUserRequest {

    @Email
    private final String email;

    @NotBlank
    private final String password;

    @NotBlank
    private final String name;

    public CreateUserRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public CreateUserCommand toCommand() {
        return new CreateUserCommand(this.email, this.password, this.name);
    }
}
