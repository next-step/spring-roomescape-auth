package roomescape.dto.request;

public class LoginRequest {

    private String password;
    private String email;

    public LoginRequest() {
    }

    public LoginRequest(String password, String email) {
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
