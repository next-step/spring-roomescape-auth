package roomescape.member.domain;

public enum MemberRole {
    GUEST,
    ADMIN;

    public static MemberRole createByName(String name) {
        if (name != null && name.equalsIgnoreCase(ADMIN.name())) {
            return ADMIN;
        }
        return GUEST;
    }
}
