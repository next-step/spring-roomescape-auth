package roomescape.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "이메일은 필수 값입니다.")
    @Email
    private String email;

    @NotBlank(message = "비밀번호는 필수 값입니다.")
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String email, String password) {
        this.password = password;
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
