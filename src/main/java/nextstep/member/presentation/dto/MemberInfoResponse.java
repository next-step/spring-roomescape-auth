package nextstep.member.presentation.dto;

import nextstep.member.Member;

public class MemberInfoResponse {

    private final Long id;
    private final String username;
    private final String password;
    private final String name;
    private final String phone;

    public MemberInfoResponse(Long id, String username, String password, String name, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public static MemberInfoResponse from(Member member) {
        return new MemberInfoResponse(member.getId(), member.getUsername(), member.getPassword(), member.getName(), member.getPhone());
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
