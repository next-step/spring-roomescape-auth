package roomescape.member.dto;

public class MemberResponseDto {

    private Long id;
    private String name;
    private String email;
    private String role;

    public MemberResponseDto(String name) {
        this.name = name;
    }

    public MemberResponseDto(Long id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public Long getId() { return id; }

    public String getName() {
        return name;
    }

    public String getEmail() { return email; }

    public String getRole() { return role; }

    @Override
    public String toString() {
        return "MemberResponseDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
