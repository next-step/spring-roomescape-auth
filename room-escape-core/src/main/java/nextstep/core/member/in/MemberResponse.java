package nextstep.core.member.in;

import nextstep.core.member.Member;

public class MemberResponse {
    private final Long id;
    private final String username;
    private final String password;
    private final String name;
    private final String phone;

    private MemberResponse(Long id, String username, String password, String name, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getUsername(), member.getPassword(), member.getName(), member.getPhone());
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
