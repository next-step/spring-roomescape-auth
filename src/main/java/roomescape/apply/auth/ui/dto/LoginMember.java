package roomescape.apply.auth.ui.dto;

import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.MemberRoleNames;

public record LoginMember(
        long id,
        String name,
        String email,
        MemberRoleNames memberRoleNames
) {
    public static LoginMember from(Member member, MemberRoleNames memberRoleNames) {
        return new LoginMember(member.getId(), member.getName(), member.getEmail(), memberRoleNames);
    }
}
