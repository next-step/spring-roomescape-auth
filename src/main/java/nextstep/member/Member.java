package nextstep.member;

public class Member {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String uuid;

    public Member(Long id, String username, String password, String name, String phone, String uuid) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.uuid = uuid;
    }

    public Member(String username, String password, String name, String phone, String uuid) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.uuid = uuid;
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

    public String getUuid() {
        return uuid;
    }

    public boolean checkWrongPassword(String password) {
        return !this.password.equals(password);
    }
}
