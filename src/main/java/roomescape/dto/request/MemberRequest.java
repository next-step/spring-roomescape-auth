package roomescape.dto.request;

public class MemberRequest {
    // TODO. 값 체크 필요
    private String name;
    private String email;
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
