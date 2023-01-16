package nextstep.core.member.in;

public class MemberLoginRequest {
    private String username;
    private String password;

    private MemberLoginRequest() {
    }

    public MemberLoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
