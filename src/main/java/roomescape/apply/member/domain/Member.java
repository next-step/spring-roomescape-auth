package roomescape.apply.member.domain;

public class Member {
    private MemberId id;
    private String name;
    private String email;
    private String password;

    protected Member() {

    }

    public Member(Long id, String name, String email, String password) {
        this.id = MemberId.of(id);
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static Member of(String name, String email, String password) {
        Member member = new Member();
        member.name = name;
        member.email = email;
        member.password = password;
        return member;
    }

    public void changeId(long id) {
        this.id = MemberId.of(id);
    }

    public Long getId() {
        return id.longValue();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}