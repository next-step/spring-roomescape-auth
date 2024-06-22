package roomescape.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class MemberRequest {

    @NotBlank(message = "이름은 필수 값입니다.")
    private String name;
    @NotBlank(message = "이메일은 필수 값입니다.")
    @Email
    private String email;
    @NotBlank(message = "비밀번호는 필수 값입니다.")
    private String password;

    public MemberRequest() {
    }

    public MemberRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
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