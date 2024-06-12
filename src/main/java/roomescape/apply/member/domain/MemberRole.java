package roomescape.apply.member.domain;

public class MemberRole {

    private Long id;
    private MemberRoleName memberRoleName;
    private Long memberId;

    protected MemberRole() {

    }

    public static MemberRole of(String roleName, Long memberId) {
        MemberRole member = new MemberRole();
        member.memberRoleName = MemberRoleName.findRoleByValue(roleName);
        member.memberId = memberId;
        return member;
    }

    public void changeId(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public MemberRoleName getMemberRoleName() {
        return memberRoleName;
    }

    public Long getMemberId() {
        return memberId;
    }
}
