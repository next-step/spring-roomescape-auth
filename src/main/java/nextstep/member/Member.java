package nextstep.member;

public class Member {
    private Long id;
    private String username;
    private Long passwordId;
    private String name;
    private String phone;

    private Member() {
    }

    public Member(Long id, String username, Long passwordId, String name, String phone) {
        this.id = id;
        this.username = username;
        this.passwordId = passwordId;
        this.name = name;
        this.phone = phone;
    }

    public Member(String username, Long passwordId, String name, String phone) {
        this.username = username;
        this.passwordId = passwordId;
        this.name = name;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Long getPasswordId() {
        return passwordId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Member)) {
            return false;
        }

        Member member = (Member) o;

        return id != null ? id.equals(member.id) : member.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
