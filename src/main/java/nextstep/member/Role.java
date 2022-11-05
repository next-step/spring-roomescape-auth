package nextstep.member;

public enum Role {
    USER, ADMIN;

    public boolean isAdmin() {
        return this == ADMIN;
    }
}
