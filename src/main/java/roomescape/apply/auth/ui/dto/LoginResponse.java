package roomescape.apply.auth.ui.dto;

import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.MemberRoleNames;

public record LoginResponse(
        String email,
        String name,
        MemberRoleNames memberRoleNames
) {
    public static LoginResponse from(Member member, MemberRoleNames memberRoleNames) {
        return new LoginResponse(member.getEmail(), member.getName(), memberRoleNames);
    }
}
