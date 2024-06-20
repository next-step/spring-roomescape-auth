package roomescape.apply.member.ui.dto;

public record MemberResponse(long id, String name) {

    public static MemberResponse from(Long memberId, String name) {
        return new MemberResponse(memberId, name);
    }
}
