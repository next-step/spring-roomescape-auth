package nextstep.app.web.member;

import nextstep.core.member.in.MemberResponse;

public class MemberWebResponse {
    private final Long id;
    private final String username;
    private final String password;
    private final String name;
    private final String phone;

    private MemberWebResponse(Long id, String username, String password, String name, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public static MemberWebResponse from(MemberResponse member) {
        return new MemberWebResponse(member.getId(), member.getUsername(), member.getPassword(), member.getName(), member.getPhone());
    }

    public boolean checkWrongPassword(String password) {
        return !this.password.equals(password);
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
