package roomescape.support;

import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.MemberRole;
import roomescape.apply.member.domain.MemberRoleName;
import roomescape.apply.member.ui.dto.MemberRequest;

import java.util.Set;

public class MemberFixture {

    public static MemberRequest memberRequest() {
        return memberRequest("newMember", "newMember@gmail.com");
    }

    public static MemberRequest memberRequest(String email) {
        return memberRequest(Set.of(), "testMember", email);
    }

    public static MemberRequest memberRequest(String newMember, String email) {
        Set<String> roleNames = Set.of(MemberRoleName.ADMIN.getValue(), MemberRoleName.GUEST.getValue());
        return memberRequest(roleNames, newMember, email);
    }

    public static MemberRequest memberRequest(Set<String> roleNames, String newMember, String email) {
        return new MemberRequest(newMember, email, "123", roleNames);
    }

    public static Member member() {
        MemberRequest request = memberRequest();
        return member(request);
    }

    public static Member member(MemberRequest request) {
        return  Member.of(request.name(), request.email(), request.password());
    }

    public static MemberRole memberRole(String roleName, long memberId) {
        return  MemberRole.of(roleName, memberId);
    }

}
