package roomescape.member.ui.dto;

import roomescape.member.domain.entity.Member;

import java.util.List;

public record MemberResponse(
        long id,
        String name,
        String role,
        String email
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getRoleName(),
                member.getEmail()
        );
    }

    public static List<MemberResponse> fromMembers(List<Member> members) {
        return members.stream().map(MemberResponse::from).toList();
    }
}
