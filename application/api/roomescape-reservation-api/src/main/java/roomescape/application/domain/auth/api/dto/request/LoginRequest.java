package roomescape.application.domain.auth.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import roomescape.application.domain.auth.service.command.LoginCommand;

public class LoginRequest {

    @Email
    private final String email;

    @NotBlank
    private final String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginCommand toCommand() {
        return new LoginCommand(this.email, this.password);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
