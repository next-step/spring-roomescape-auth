package nextstep.auth;

public class AuthMember {
    private final Long id;

    private AuthMember(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public static AuthMember from(String id) {
        return new AuthMember(Long.parseLong(id));
    }
}
