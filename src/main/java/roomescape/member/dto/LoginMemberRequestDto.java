package roomescape.member.dto;

import jakarta.validation.constraints.NotEmpty;

public class LoginMemberRequestDto {

    @NotEmpty(message = "이이디와 비밀번호를 확인해주세요")
    private String email;
    @NotEmpty(message = "이이디와 비밀번호를 확인해주세요")
    private String password;

    public LoginMemberRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
