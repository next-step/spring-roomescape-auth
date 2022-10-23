package nextstep.member;

public class MemberResponse {

    private final Long id;
    private final String username;
    private final String password;
    private final String name;
    private final String phone;
    private final String role;

    public MemberResponse(
        Long id,
        String username,
        String password,
        String name,
        String phone,
        String role
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(
            member.getId(),
            member.getUsername(),
            member.getPassword(),
            member.getName(),
            member.getPhone(),
            member.getRole()
        );
    }

    public Long getId() {
        return id;
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

    public String getRole() {
        return role;
    }
}
