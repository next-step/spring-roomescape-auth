package roomescape.member.ui.dto;

import roomescape.member.domain.entity.Member;

public record MemberResponse(
        long id,
        String name,
        String email
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }
}
