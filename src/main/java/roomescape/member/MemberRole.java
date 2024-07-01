package roomescape.member;

import roomescape.error.exception.IllegalMemberRoleException;

public enum MemberRole {
    ADMIN,
    MEMBER;

    public static MemberRole of(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalMemberRoleException();
        }
    }
}
