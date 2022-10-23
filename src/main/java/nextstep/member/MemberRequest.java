package nextstep.member;

import java.util.List;

public class MemberRequest {
    private String username;
    private String password;
    private String name;
    private String phone;

    public MemberRequest(String username, String password, String name, String phone) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
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

    public Member toEntity(Long passwordId) {
        // FIXME : 유저 권한 하드코딩
        return new Member(username, passwordId, name, phone, List.of(MemberRole.USER));
    }
}
