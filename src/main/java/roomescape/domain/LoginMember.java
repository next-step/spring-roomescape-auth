package roomescape.domain;

public class LoginMember {
    private Long id;
    private String email;
    private String name;

    public LoginMember(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
