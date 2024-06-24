package roomescape.domain.member.domain;

public enum Role {

    ADMIN("ADMIN", "관리자"),
    USER("USER", "일반");


    private final String role;
    private final String description;

    Role(String role, String description) {
        this.role = role;
        this.description = description;
    }

    public String getRole() {
        return role;
    }

    public String getDescription() {
        return description;
    }
}
