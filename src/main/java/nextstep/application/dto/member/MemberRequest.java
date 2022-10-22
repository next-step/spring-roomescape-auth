package nextstep.application.dto.member;

public class MemberRequest {

    private String username;
    private String password;
    private String name;
    private String phone;

    private MemberRequest() {
    }

    public MemberRequest(String username, String password, String name, String phone) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
