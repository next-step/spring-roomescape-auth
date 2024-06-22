package roomescape.domain.member.service.dto;

public class AdminMemberResponse {

    private final Long id;
    private final String name;
    private final String role;

    public AdminMemberResponse(Long id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
}
