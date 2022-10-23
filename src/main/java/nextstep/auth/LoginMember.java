package nextstep.auth;

public class LoginMember {

    private final Long id;

    public LoginMember(Long id) {
        this.id = id;
    }

    public static LoginMember from(String id) {
        return new LoginMember(Long.parseLong(id));
    }

    public Long getId() {
        return id;
    }
}
