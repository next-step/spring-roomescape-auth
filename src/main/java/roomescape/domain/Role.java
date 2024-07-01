package roomescape.domain;

public class Role {

    private Long id;
    private RoleType name;

    public Role(Long id, RoleType name) {
        this.id = id;
        this.name = name;
    }

    public Role(RoleType roleType) {
        this.name = roleType;
    }

    public Long getId() {
        return id;
    }

    public RoleType getName() {
        return name;
    }
}
