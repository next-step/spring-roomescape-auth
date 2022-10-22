package nextstep.member;

public class MemberResponse {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;

    public MemberResponse(Long id, String username, String password, String name, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public static MemberResponse of(Member member) {
        return new MemberResponse(
            member.getId(),
            member.getUsername(),
            member.getPassword(),
            member.getName(),
            member.getPhone()
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
}
